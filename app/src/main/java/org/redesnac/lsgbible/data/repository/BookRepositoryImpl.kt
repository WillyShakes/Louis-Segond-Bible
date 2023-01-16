package org.redesnac.lsgbible.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.redesnac.lsgbible.data.remote.BibleApi1
import org.redesnac.lsgbible.data.remote.BibleApi2
import org.redesnac.lsgbible.data.remote.dto.book.BookDto
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import org.redesnac.lsgbible.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(val api1: BibleApi1, val api2: BibleApi2): BookRepository {
    private val tag: String = "BookRepositoryImpl"

    override suspend fun fetchBooks(): List<BookDto> {
        Log.d(tag,"fetchBooks")
        return withContext(Dispatchers.IO) {
            api1.fetchBooks().results
        }
    }

    override suspend fun fetchBook(name: String): List<VerseDto> {
        Log.d(tag,"fetchBooks $name")
        return withContext(Dispatchers.IO){
            val results = api2.fetchBook(name = name).results[0]
            Log.d(tag,"fetchBooks $results")
            results.verses.segond_1910.the1.map { it.value }
        }
    }
}
