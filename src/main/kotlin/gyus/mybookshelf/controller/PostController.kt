package gyus.mybookshelf.controller

import gyus.mybookshelf.model.dto.PostDTO
import gyus.mybookshelf.service.PostService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

data class CreatePostRequest(
    val memberId: Long,
    val title: String,
    val content:String,
    val isEpisode:Boolean
)

data class UpdatePostRequest(
    var id: Long,
    val title: String,
    val content: String,
    val isEpisode: Boolean
)

@Tag(name="게시글", description = "게시글 API")
@RestController
@RequestMapping("/posts")
class PostController(private val postService: PostService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createPost(@RequestBody createPostRequest: CreatePostRequest): PostDTO {
        return postService.writePost(createPostRequest)
    }

    @GetMapping
    fun getPosts(
        @RequestParam condition: String = "title",
        @RequestParam value:String = "",
        @RequestParam page: Int = 1,
        @RequestParam size: Int = 10
    ): List<PostDTO> {
        if (condition == "id") {
            return postService.findAllPost(condition, value)
        }
        val pageable = PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC, "id"))
        return postService.findAllPost(condition, value, pageable)
    }

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): PostDTO {
        return postService.findPostById(id)
    }

    @PutMapping("/{id}")
    fun updatePost(@PathVariable id:Long, @RequestBody updatePostRequest: UpdatePostRequest): PostDTO {
         updatePostRequest.id = id
        return postService.updatePost(updatePostRequest)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id:Long): String {
        postService.deletePost(id)
        return "OK"
    }

}