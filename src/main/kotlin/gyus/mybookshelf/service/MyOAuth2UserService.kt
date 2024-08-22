package gyus.mybookshelf.service

import gyus.mybookshelf.model.Member
import gyus.mybookshelf.repository.MemberRepository
import gyus.mybookshelf.security.MemberDetails
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class MyOAuth2UserService(
    private val memberRepository: MemberRepository
): DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val user = super.loadUser(userRequest)
        val attributes = user.attributes
        val providerId = attributes["sub"] as String
        val email = attributes["email"] as String
        val name = attributes["name"] as String
        val member = memberRepository.findByEmail(email) ?: memberRepository.save(
            Member(email = email, name = name, password = "", providerId= providerId)
        )
        return MemberDetails(member)
    }

}