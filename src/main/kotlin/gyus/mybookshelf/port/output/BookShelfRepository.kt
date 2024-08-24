package gyus.mybookshelf.port.output

import gyus.mybookshelf.model.BookShelf
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component


interface JpaBookShelfRepository : JpaRepository<BookShelf, Long>{
    fun deleteByMemberIdAndBookId(memberId: Long,bookId: Long)
    fun deleteAllByMemberId(memberId: Long)
}

    @Component
    interface BookShelfRepository {
        fun save(bookShelf: BookShelf): BookShelf
        fun findByMemberId(memberId: Long): List<BookShelf>
        fun findAll(): List<BookShelf>
        fun deleteByMemberIdAndBookId(memberId:Long, bookId: Long)
        fun deleteAllByMemberId(memberId: Long)
    }

