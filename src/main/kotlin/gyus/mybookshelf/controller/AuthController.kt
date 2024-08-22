package gyus.mybookshelf.controller

import gyus.mybookshelf.jwt.JwtUtil
import gyus.mybookshelf.security.MemberDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class AuthRequest(
    val email: String,
    val password:String
)


data class ChangeSecretKeyRequest(
    val email: String,
    val secretKey: String
)

@RestController
class AuthController(
    private val jwtUtil: JwtUtil,
    private val passwordEncoder: PasswordEncoder,
    private val memberDetailsService: MemberDetailsService,
    private val userSecretKeys: MutableMap<String, String >
) {

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody authRequest: AuthRequest ):String {
        val userDetails = memberDetailsService.loadUserByUsername(authRequest.email)

       val claims =  hashMapOf(
            "email" to authRequest.email,
            "role" to userDetails.authorities.first().authority
        )

        if (passwordEncoder.matches(authRequest.password, userDetails.password)) {
            return jwtUtil.generateToken(authRequest.email, claims)
        }

        return "Authentication Failed"
    }

    @PostMapping("/changeSecretKey")
    fun changeSecretKey(@RequestBody changeSecretKeyRequest: ChangeSecretKeyRequest):String {
      if (changeSecretKeyRequest.secretKey.length < 32) {
          throw  IllegalArgumentException("시크릿키는 32자 이상이어야 합니다.")
      }
        userSecretKeys[changeSecretKeyRequest.email] = changeSecretKeyRequest.secretKey
        return "Secret key changed successfully"
    }
}