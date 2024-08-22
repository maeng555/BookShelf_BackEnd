package gyus.mybookshelf.controller

import gyus.mybookshelf.service.GoogleOAuth2Service
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Controller
@RequestMapping("/login/oauth2")
class OAuthController(
    private val googleOAuth2Service: GoogleOAuth2Service
) {

    @GetMapping("/code/google")
    fun googleLogin(
        @RequestParam code:String,
        @RequestParam state:String,
        @RequestParam scope:String
    ):ResponseEntity<String> {
        println("code $code")
        println("scope $scope")

        val response = googleOAuth2Service.getAccessToken(code, "http://localhost:8080/login/oauth2/code/google")
        println(response)
        val accessToken = response.access_token
        println("accessToken $accessToken")

        val userInfo = googleOAuth2Service.getUserInfo(accessToken)
        println(userInfo)
        return ResponseEntity.ok().body(userInfo.name)
    }

    @GetMapping("/success")
    fun success(token: OAuth2AuthenticationToken, model: Model): String {
        val principal = token.principal as OAuth2User
        println(principal.attributes)
        println(principal.name)
        model.addAttribute("name", principal.name)
        return "oauth_success"
    }
}