package gyus.mybookshelf.port.input

import gyus.mybookshelf.model.Book
import gyus.mybookshelf.model.BookShelf
import jakarta.transaction.Transactional


interface BookShelfPort {
        fun createBookShelf(memberId:Long, bookId:Long): BookShelf
        fun getMyBookFromBookShelf(memberId:Long): List<Book>
        fun allBookShelf(): List<BookShelf>
        @Transactional
        fun deleteMyBookShelf(memberId:Long, bookId:Long): Boolean
        @Transactional
        fun deleteAllBookShelf(memberId:Long): Boolean
    }
