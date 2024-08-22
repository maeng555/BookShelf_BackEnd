package gyus.mybookshelf.controller

import gyus.mybookshelf.service.PostService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class HelloController(
    val postService: PostService
) {

    @GetMapping("/")
    fun index():String {
        return "index"
    }

    @GetMapping("/hello")
    fun hello(): String {
        return "hello"
    }

    @GetMapping("/home")
    fun home(authentication: Authentication, model:Model): String {
        model.addAttribute("name", authentication.name)
        model.addAttribute("posts", postService.findPostsWithTitleLike(""))
        return "home"
    }

}