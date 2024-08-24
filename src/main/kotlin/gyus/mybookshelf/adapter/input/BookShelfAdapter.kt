package gyus.mybookshelf.adapter.input

import gyus.mybookshelf.model.Book
import gyus.mybookshelf.model.BookShelf
import gyus.mybookshelf.port.input.BookShelfPort
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class BookShelfRequest(
    val memberId: Long,
    val bookId: Long
)
@RestController
@RequestMapping("/bookshelves")
class BookShelfAdapter(
    private val bookShelfPort: BookShelfPort
) {
    @PostMapping
    fun createBookShelf(@RequestBody bcr:BookShelfRequest):BookShelf{
        return bookShelfPort.createBookShelf(bcr.memberId ,bcr.bookId)
    }
    @GetMapping("/member/{memberId}")
    fun getMyBookFromBookShelf(@PathVariable memberId:Long): List<Book>{
        return bookShelfPort.getMyBookFromBookShelf(memberId)
    }
    @GetMapping
    fun allBookShelf(): List<BookShelf>{
        return bookShelfPort.allBookShelf()
    }
    @DeleteMapping("/member")
    fun deleteMyBookShelf(@RequestBody bcr:BookShelfRequest):String{
        bookShelfPort.deleteMyBookShelf(bcr.memberId,bcr.bookId)
        return "ok"
    }
    @DeleteMapping("/member/{memberId}")
    fun deleteAllMyBookShelf(@PathVariable memberId:Long): String{
        bookShelfPort.deleteAllBookShelf(memberId)
        return "ok"
    }

}