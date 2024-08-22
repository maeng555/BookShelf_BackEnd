package gyus.mybookshelf.security

import gyus.mybookshelf.model.BaseMember
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class MemberDetails(
    private val member:BaseMember,
): UserDetails, OAuth2User {
    override fun getName(): String {
        return member.email
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return mutableMapOf("role" to member.role, "email" to member.email)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(GrantedAuthority { member.role })
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}