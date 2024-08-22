package gyus.mybookshelf.controller

import gyus.mybookshelf.model.dto.BookDTO
import gyus.mybookshelf.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/books")
class BookController(private val bookService:BookService) {

    @GetMapping
    fun findAll(): List<BookDTO> {
        return bookService.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id:Long): ResponseEntity<BookDTO> {
        val book = bookService.findById(id)
        return if (book != null) {
            ResponseEntity.ok(book)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun save(@RequestBody bookDto: BookDTO): BookDTO {
        return bookService.save(bookDto)
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id:Long, @RequestBody bookDto: BookDTO): BookDTO {
        return bookService.update(id, bookDto)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): String {
        bookService.deleteById(id)
        return "OK"
    }
}