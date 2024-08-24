package gyus.mybookshelf.service

import gyus.mybookshelf.model.Book
import gyus.mybookshelf.model.BookShelf
import gyus.mybookshelf.port.input.BookShelfPort
import gyus.mybookshelf.port.output.BookShelfRepository
import gyus.mybookshelf.repository.BookRepository
import gyus.mybookshelf.repository.MemberRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull


@Service
class BookShelfService(
    private val bookShelfRepository: BookShelfRepository,
    private val bookRepository: BookRepository,
    private val memberRepository: MemberRepository
): BookShelfPort {
    override fun createBookShelf(memberId: Long, bookId: Long): BookShelf {
        val member =memberRepository.findById(memberId).getOrNull()
        val book= bookRepository.findById(bookId).getOrNull()
        val bookShelf =BookShelf(member=member!!, book = book!!)
        return bookShelfRepository.save(bookShelf)
    }

    override fun getMyBookFromBookShelf(memberId: Long): List<Book> {
        val bookShelfList = bookShelfRepository.findByMemberId(memberId)
        return bookShelfList.map { it.book }
    }

    override fun allBookShelf(): List<BookShelf> {
        return bookShelfRepository.findAll()
    }

    override fun deleteMyBookShelf(memberId: Long, bookId: Long): Boolean {
        bookShelfRepository.deleteByMemberIdAndBookId(memberId,bookId)
        return true

    }

    override fun deleteAllBookShelf(memberId: Long): Boolean {
       bookShelfRepository.deleteAllByMemberId(memberId)
        return true
    }


}