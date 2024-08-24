package gyus.mybookshelf.repository

import gyus.mybookshelf.model.Book
import gyus.mybookshelf.model.BookShelf
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long>



