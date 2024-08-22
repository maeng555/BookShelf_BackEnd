package gyus.mybookshelf.jwt

import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtUtilTest {

    @Autowired
    lateinit var jwtUtil: JwtUtil

    val testToken = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im1lbWJlcjFAZW1haWwuY29tIiwicm9sZSI6Ik1FTUJFUiIsInN1YiI6InRlc3QiLCJpYXQiOjE3MTc0MjAxODUsImV4cCI6MTcxNzQyMzc4NX0.xu3cUgwsckVmyoclerAI_Lwve4c6ElMMYxhQh4DuSQ4"

    @Test
    fun generateToken() {
        val key = Keys.hmacShaKeyFor(jwtUtil.secretKey.toByteArray())
        val token = jwtUtil.generateToken("test", hashMapOf("email" to "member1@email.com", "role" to "MEMBER"))
        println(token)

        val payload = jwtUtil.getClaims(token).payload
        assert(payload["role"] == "MEMBER")
        assert(payload["email"] == "member1@email.com")
        assert(payload.subject == "test")
    }

    @Test
    fun getUsername() {
        val result = jwtUtil.getUsername(
            testToken)
        assert(result == "test")
    }

    @Test
    fun isTokenExpired() {
        val result = jwtUtil.isTokenExpired(testToken)
        assert(!result)
    }

    @Test
    fun validateToken() {
        val result = jwtUtil.validateToken(testToken, "test")
        assert(result)
    }
}