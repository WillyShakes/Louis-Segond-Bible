package org.redesnac.lsgbible.data.remote

import org.redesnac.lsgbible.data.remote.dto.book.BooksResponse
import org.redesnac.lsgbible.data.remote.dto.verse.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BibleApi1 {
    @GET("books?language=fr")
    suspend fun fetchBooks(): BooksResponse
}

interface BibleApi2 {
    @GET("api")
    suspend fun fetchBook(@Query("bible") bible: String = "segond_1910", @Query("reference") name: String): BookResponse
}