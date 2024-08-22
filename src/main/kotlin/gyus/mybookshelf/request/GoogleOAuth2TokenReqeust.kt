package gyus.mybookshelf.request

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name="google-oauth2-token", url="https://oauth2.googleapis.com/token")
interface GoogleOAuth2TokenRequest {

    @PostMapping(headers = ["Content-Type=application/x-www-form-urlencoded"])
    fun getAccessToken(
        @RequestParam("client_id") clientId:String,
        @RequestParam("client_secret") clientSecret:String,
        @RequestParam("code") authorizationCode: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("grant_type") grantType: String = "authorization_code"
    ): AccessTokenResponse
}

data class AccessTokenResponse(
    val access_token:String,
    val expires_in: Int,
    val token_type: String,
    val refresh_token: String?,
    val scope: String
)