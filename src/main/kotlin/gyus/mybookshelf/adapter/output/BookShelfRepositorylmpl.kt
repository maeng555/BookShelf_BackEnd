package gyus.mybookshelf.adapter.output

import gyus.mybookshelf.model.BookShelf
import gyus.mybookshelf.port.output.BookShelfRepository
import gyus.mybookshelf.port.output.JpaBookShelfRepository
import gyus.mybookshelf.repository.BookRepository
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Primary
@Repository
class InMemoryBookShelfRepository: BookShelfRepository {

    companion object{
        private val bookShelfList= mutableListOf<BookShelf>()
    }

    override fun save(bookShelf: BookShelf): BookShelf {
       bookShelf.id = bookShelfList.size.toLong()+1
        bookShelfList.add(bookShelf)
        return bookShelf

    }

    override fun findByMemberId(memberId: Long): List<BookShelf> {
        return bookShelfList.filter { it.member.id==memberId }
    }

    override fun findAll(): List<BookShelf> {
        return bookShelfList
    }

    override fun deleteByMemberIdAndBookId(memberId: Long, bookId: Long) {
       bookShelfList.removeIf{it.member.id==memberId&&it.book.id==bookId}
    }

    override fun deleteAllByMemberId(memberId: Long) {
        bookShelfList.removeIf{it.member.id==memberId}
    }
}
@Repository
class RdbBookShelfRepository(
    private val jpaBookShelfRepository: JpaBookShelfRepository
):BookShelfRepository{
    override fun save(bookShelf: BookShelf): BookShelf {
        return jpaBookShelfRepository.save(bookShelf)
    }

    override fun findByMemberId(memberId: Long): List<BookShelf> {
        return jpaBookShelfRepository.findAll().filter { it.member.id==memberId }
    }

    override fun findAll(): List<BookShelf> {
       return jpaBookShelfRepository.findAll()
    }

    override fun deleteByMemberIdAndBookId(memberId: Long, bookId: Long) {
        jpaBookShelfRepository.deleteByMemberIdAndBookId(memberId,bookId)
    }

    override fun deleteAllByMemberId(memberId: Long) {
        jpaBookShelfRepository.deleteAllByMemberId(memberId)
    }

}




