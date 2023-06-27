package org.redesnac.lsgbible.data.repository

import kotlinx.coroutines.withContext
import org.redesnac.lsgbible.data.remote.BibleApi1
import org.redesnac.lsgbible.data.remote.BibleApi2
import org.redesnac.lsgbible.data.remote.dto.book.BookDto
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import org.redesnac.lsgbible.domain.CoroutineDispatcherProvider
import org.redesnac.lsgbible.domain.book.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    val api1: BibleApi1,
    val api2: BibleApi2,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider):
    BookRepository {

    override suspend fun fetchBooks(): List<BookDto> {
        return withContext(coroutineDispatcherProvider.io) {
            api1.fetchBooks().results
        }
    }

    override suspend fun fetchBook(name: String): List<VerseDto> {
        return withContext(coroutineDispatcherProvider.io){
            val results = api2.fetchBook(name = name).results[0]
            results.verses.segond_1910.the1.map { it.value }
        }
    }
}
