package gyus.mybookshelf

import gyus.mybookshelf.model.Member
import gyus.mybookshelf.repository.MemberRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
// 무슨 기능인지 주석을 달아주세요
// DataJpaTest는 JPA 테스트를 위한 어노테이션입니다.
// @TestInstance 어노테이션을 사용하면 테스트 클래스의 인스턴스를 어떻게 생성할지 설정할 수 있습니다.
// TestInstance.Lifecycle.PER_CLASS를 사용하면 테스트 클래스의 인스턴스를 한 번만 생성하고 모든 테스트 메서드에서 재사용합니다.
// 기본값은 TestInstance.Lifecycle.PER_METHOD입니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberTest {

    @Autowired
    lateinit var memberRepository: MemberRepository

    @BeforeAll
    fun setup() {
        val member = Member(name = "member1", email = "member1@email.com", "1234")
        memberRepository.save(member)
    }

    @Test
    fun `멤버 CRUD 테스트`() {
        val member = Member(name = "member2", email = "member2@gmail.com" , "1234")
        memberRepository.save(member)

        val members = memberRepository.findAll()

        assert(members.size == 2)

        // get member
        val member1 = memberRepository.findById(1L).get()
        assert(member1.name == "member1")

        // update member email
        member1.email = "member1updated@email.com"
        // save를 해주면 쿼리가 실행됨.. (안되는데?)
        // @Transactional 이 있으면 save 안해도 알아서 커밋
        memberRepository.save(member1)

        // get member1

        // 실제로 쿼리를 실행하지 않았는데 어떻게 테스트가 성공하는가?
        // 영속성 컨텍스트 때문. 캐시된 member1을 영속성 컨텍스트에서 가져옴
        val member1Updated = memberRepository.findById(1L).get()
        println(">>>> ${member1Updated.email}")
        assert(member1Updated.email == member1.email)

        // delete member1
        memberRepository.delete(member1)
    }

}