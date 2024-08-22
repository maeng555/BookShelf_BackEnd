package gyus.mybookshelf.security

import gyus.mybookshelf.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MemberDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService{

    override fun loadUserByUsername(email: String?): UserDetails {
        if (email.isNullOrEmpty()) {
            throw IllegalArgumentException("사용자가 없습니다.")
        }
        val member = memberRepository.findByEmail(email) ?: throw IllegalArgumentException("사용자가 없습니다.")

        println("member: $member, role : ${member.role}")

        return MemberDetails(member)

    }
}