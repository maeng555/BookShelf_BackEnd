package gyus.mybookshelf.service

import gyus.mybookshelf.controller.CreatePostRequest
import gyus.mybookshelf.controller.UpdatePostRequest
import gyus.mybookshelf.model.BaseMember
import gyus.mybookshelf.model.Episode
import gyus.mybookshelf.model.Post
import gyus.mybookshelf.model.dto.PostDTO
import gyus.mybookshelf.repository.EpisodeRepository
import gyus.mybookshelf.repository.MemberRepository
import gyus.mybookshelf.repository.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val episodeRepository: EpisodeRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun writePost(createPostRequest: CreatePostRequest): PostDTO {
        val member = memberRepository.findById(createPostRequest.memberId).orElseThrow {
            throw IllegalArgumentException("해당 멤버가 없습니다.")
        }

        val post = Post(
            title = createPostRequest.title,
            content = createPostRequest.content,
            member = member,
            isEpisode = createPostRequest.isEpisode
        )
        postRepository.save(post)

        if (createPostRequest.isEpisode) {
            val episode = Episode(post = post)
            episodeRepository.save(episode)
        }
        return PostDTO.toDto(post)

    }

    fun findPostByWriter(writer: BaseMember): List<Post> {
        return postRepository.findByMember(writer)
    }

    @Transactional(readOnly = true)
    fun findAllPost(condition: String, value:String, pageable: Pageable = PageRequest.of(0, 1)): List<PostDTO> {
        return postRepository.findPostsByCondition(condition, value, pageable)
    }

    // 읽기 전용 쿼리 최적화
    @Transactional(readOnly = true)
    fun findAllWithMember(): List<Post> {
        return postRepository.findAll()
    }


    fun findPostsWithTitleLike(title: String): List<Post> {
        return postRepository.findPostsWithTitleLike(title)
    }

    fun findPostsSelectTitle(): List<PostDTO> {
        return postRepository.findPostsSelectTitle()
    }

    fun findPostPagingWithSort(pageable: Pageable): List<PostDTO> {
        return postRepository.findPostPagingWithSort(pageable)
    }

    fun findPostsByCondition(condition: String, value: String, pageable: Pageable): List<PostDTO> {
        return postRepository.findPostsByCondition(condition, value, pageable)
    }

    fun findPostById(id: Long): PostDTO {
        val post = postRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("해당 게시물이 없습니다.")
        }
        return PostDTO.toDto(post)
    }

    @Transactional
    fun updatePost(updatePostRequest: UpdatePostRequest): PostDTO {
        val post = postRepository.findById(updatePostRequest.id).orElseThrow {
            throw IllegalArgumentException("해당 게시물이 없습니다.")
        }

        // 기존 포스트의 isEpisode값과 updatePostRequest의 isEpisode 값이 다르면 다음 코드를 실행한다.
        if (post.isEpisode != updatePostRequest.isEpisode) {
            if (updatePostRequest.isEpisode) {
                val episode = Episode(post = post)
                episodeRepository.save(episode)
            } else {
                episodeRepository.findByPost(post)?.let {
                    episodeRepository.delete(it)
                }
            }
        }

        post.title = updatePostRequest.title
        post.content =updatePostRequest.content
        post.isEpisode = updatePostRequest.isEpisode
        return PostDTO.toDto(post)
    }

    fun deletePost(id: Long) {
        val post = postRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("해당 게시물이 없습니다.")
        }
        post.isEpisode.let {
            episodeRepository.findByPost(post)?. let {
                episodeRepository.delete(it)
            }
            postRepository.deleteById(id)
        }
    }
}