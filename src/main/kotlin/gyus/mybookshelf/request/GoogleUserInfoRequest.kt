package gyus.mybookshelf.request

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(name = "google-userinfo", url = "https://www.googleapis.com/oauth2/v3")
interface GoogleUserInfoRequest {

    @GetMapping("/userinfo")
    fun getUserInfo(
        @RequestParam("access_token") accessToken: String
    ): GoogleUserInfoResponse

}

data class GoogleUserInfoResponse(
    val sub: String,
    val name: String,
    val given_name: String,
    val family_name: String,
    val picture: String,
    val email: String,
    val email_verified: Boolean
)