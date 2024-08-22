package gyus.mybookshelf

import gyus.mybookshelf.model.Author
import gyus.mybookshelf.model.Member
import gyus.mybookshelf.model.Post
import gyus.mybookshelf.repository.MemberRepository
import gyus.mybookshelf.repository.PostRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInit(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val passwordEncoder:PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        // post 추가
        val posts = mutableListOf<Post>()
        for (i in 1..2) {
            val member = Member(name = "member$i", email = "member$i@email.com", password = passwordEncoder.encode("1234$i"))
            memberRepository.save(member)

            val post = Post(title = "title$i", content = "content$i", member = member, isEpisode = true)
            posts.add(post)
        }
        postRepository.saveAll(posts)

        val author = Author(name="author1", email="author1@email.com", password= passwordEncoder.encode("1234"))
        memberRepository.save(author)
    }
}