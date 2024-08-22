package gyus.mybookshelf.oauth

import gyus.mybookshelf.jwt.JwtUtil
import gyus.mybookshelf.security.MemberDetails
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    private val jwtUtil: JwtUtil
): AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {

        if (authentication != null && response != null) {
            val memberDetails = authentication.principal as MemberDetails
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            val claims = hashMapOf(
                "email" to memberDetails.name,
                "role" to memberDetails.authorities.first().authority
            )
            println(claims)
            response.writer.write(jwtUtil.generateToken(memberDetails.name, claims))
        }
        return
    }
}