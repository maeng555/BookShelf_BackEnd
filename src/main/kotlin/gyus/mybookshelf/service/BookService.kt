package gyus.mybookshelf.service

import gyus.mybookshelf.model.Book
import gyus.mybookshelf.model.dto.BookDTO
import gyus.mybookshelf.repository.BookRepository
import gyus.mybookshelf.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<BookDTO> {
        return bookRepository.findAll().map { BookDTO.fromEntity(it) }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): BookDTO? {
        return bookRepository.findById(id).map{BookDTO.fromEntity(it)}.getOrNull()
    }

    @Transactional
    fun save(bookDto:BookDTO): BookDTO {
        val author = memberRepository.findAuthorById(bookDto.memberId)
            ?: throw  IllegalArgumentException("id : ${bookDto.memberId}에 해당하는 작가가 없습니다.")

        val book = Book(
            content = bookDto.content,
            author = author,
            authorName = author.name,
            title = bookDto.title,
            publisher =  bookDto.publisher,
            isbn = bookDto.isbn
        )
        val savedBook = bookRepository.save(book)
        return BookDTO.fromEntity(savedBook)
    }

    @Transactional
    fun update(id:Long, bookDto:BookDTO): BookDTO {
        val book = findById(id)  ?: throw  IllegalArgumentException("id : ${id}에 해당하는 책이 없습니다.")

        val author = memberRepository.findAuthorById(bookDto.memberId)
            ?: throw  IllegalArgumentException("id : ${bookDto.memberId}에 해당하는 작가가 없습니다.")

        val toUpdatebook = Book(
            id = book.id!!,
            content = bookDto.content,
            author = author,
            authorName =  author.name,
            title = bookDto.title,
            publisher =  bookDto.publisher,
            isbn =  bookDto.isbn
        )

        val updatedBook = bookRepository.save(toUpdatebook)
        return BookDTO.fromEntity(updatedBook)

    }

    @Transactional
    fun deleteById(id:Long) {
        bookRepository.deleteById(id)
    }
}