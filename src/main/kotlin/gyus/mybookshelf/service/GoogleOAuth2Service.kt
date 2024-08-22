package gyus.mybookshelf.service

import gyus.mybookshelf.request.AccessTokenResponse
import gyus.mybookshelf.request.GoogleOAuth2TokenRequest
import gyus.mybookshelf.request.GoogleUserInfoRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GoogleOAuth2Service(
    @Value("\${spring.security.oauth2.client.registration.google.client-id}")
    private val clientId:String,
    @Value("\${spring.security.oauth2.client.registration.google.client-secret}")
    private val clientSecret:String,
    private val googleOAuth2TokenRequest: GoogleOAuth2TokenRequest,
    private val googleUserInfoRequest: GoogleUserInfoRequest
) {

    fun getAccessToken(authorizationCode: String, redirectUri:String): AccessTokenResponse {
        return googleOAuth2TokenRequest.getAccessToken(
            clientId = clientId,
            clientSecret = clientSecret,
            authorizationCode = authorizationCode,
            redirectUri = redirectUri
        )
    }

    fun getUserInfo(accessToken:String) = googleUserInfoRequest.getUserInfo(accessToken)

}