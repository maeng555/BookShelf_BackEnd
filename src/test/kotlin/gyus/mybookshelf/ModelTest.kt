package gyus.mybookshelf

import gyus.mybookshelf.model.Author
import gyus.mybookshelf.model.BaseMember
import gyus.mybookshelf.model.Member
import gyus.mybookshelf.repository.MemberRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataJpaTest(showSql = true)
class ModelTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    fun getAllUsers() {

        val member1 = Member(name= "member1", email= "member1@email.com", "1234")
        val author1 = Author(name= "author1", email= "author1@email.com", "1234")
        val member2 = Member(name= "member2", email= "member2@email.com", "1234")
        val author2 = Author(name= "author2", email= "author2@email.com", "1234")

        memberRepository.save(member1)
        memberRepository.save(author1)
        memberRepository.save(member2)
        memberRepository.save(author2)

        val baseMembers:List<BaseMember> = memberRepository.findAll()
        for (member in baseMembers) {
            when (member) {
                is Author -> println("<AUTHOR> ${member.name}")
                is Member -> println("<MEMBER> ${member.name}")
            }
        }
    }

    @Test
    fun `이름이 20자 넘어가면 에러가납니다`() {
        val baseMember = Member(name="12345678901234567890!", email = "test@mail.com", "1234")
        assertThrows<DataIntegrityViolationException> {
            memberRepository.save(baseMember)
        }
    }

    @Test
    fun `이메일이 유니크한지 테스트`() {
        val baseMember = Member(name="user1", email="user1@email.com", "1234")
        val baseMember2 = Member(name="user2", email="user1@email.com", "1234")
        assertThrows<DataIntegrityViolationException> {
            memberRepository.save(baseMember)
            memberRepository.save(baseMember2)
        }
    }
}