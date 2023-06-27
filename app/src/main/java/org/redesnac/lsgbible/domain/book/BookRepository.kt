package org.redesnac.lsgbible.domain.book

import org.redesnac.lsgbible.data.remote.dto.book.BookDto
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto

interface BookRepository {
    suspend fun fetchBooks():List<BookDto>
    suspend fun fetchBook(name: String):List<VerseDto>
}