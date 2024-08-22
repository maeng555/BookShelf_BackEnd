package gyus.mybookshelf.model.dto

import gyus.mybookshelf.model.Book

data class BookDTO(
    var id: Long? = null,
    val content: String,
    val authorName: String?,
    val title: String,
    val publisher: String,
    val isbn: String,
    var memberId: Long
) {
    companion object {
        fun fromEntity(book: Book): BookDTO {
            return BookDTO(
                id = book.id,
                content = book.content,
                authorName = book.author.name,
                title = book.title,
                publisher = book.publisher,
                isbn = book.isbn,
                memberId = book.author.id,
            )
        }
    }
}