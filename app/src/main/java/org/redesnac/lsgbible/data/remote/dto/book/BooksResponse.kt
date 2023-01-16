package org.redesnac.lsgbible.data.remote.dto.book

import org.redesnac.lsgbible.domain.model.book.Book

data class BooksResponse (
    val errorLevel: Long,
    val results: List<BookDto>,
    val errors: List<Any?>
)

data class BookDto (
    val chapters: Long,
    val name: String,
    val id: Long,
    val chapter_verses: Map<String, Long>,
    val shortname: String
) {
    fun toBook(): Book {
        return Book(
            name = name,
            chapters = chapters
        )
    }
}
