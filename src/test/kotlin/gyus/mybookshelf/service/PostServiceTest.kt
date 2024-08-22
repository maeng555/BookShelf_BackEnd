package gyus.mybookshelf.service

import gyus.mybookshelf.controller.CreatePostRequest
import gyus.mybookshelf.model.Author
import gyus.mybookshelf.repository.EpisodeRepository
import gyus.mybookshelf.repository.MemberRepository
import gyus.mybookshelf.repository.PostRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.transaction.TestTransaction
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

// 서비스레이어를 주입받으려면 @SpringBootTest를 사용해야 합니다.
@SpringBootTest
class PostServiceTest {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var episodeRepository: EpisodeRepository

    @Autowired
    lateinit var postService: PostService

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    fun writePost() {
        val member = Author(name = "andy", email = "andy@email.com", "1234")
        memberRepository.save(member)

        assertThrows<RuntimeException> {
            postService.writePost(CreatePostRequest(1, "test", "content", true))
            throw RuntimeException("Test exception")
        }

        TestTransaction.end()
        val posts = postRepository.findByMember(member)
        assertEquals(0, posts.size)


        // 중간에 에러나서 롤백되었기에 episode도 member도 저장되지 않음.
        val episodeSize = episodeRepository.count()
        assertEquals(0, episodeSize)

        val memberSize = memberRepository.count()
    }
}