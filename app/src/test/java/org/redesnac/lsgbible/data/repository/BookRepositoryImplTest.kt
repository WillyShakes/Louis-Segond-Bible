package org.redesnac.lsgbible.data.repository

import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.redesnac.lsgbible.TestCoroutineRule
import org.redesnac.lsgbible.data.remote.BibleApi1
import org.redesnac.lsgbible.data.remote.BibleApi2
import org.redesnac.lsgbible.data.remote.dto.book.BookDto
import org.redesnac.lsgbible.data.remote.dto.book.BooksResponse
import org.redesnac.lsgbible.data.remote.dto.verse.BookResponse
import org.redesnac.lsgbible.data.remote.dto.verse.Nav
import org.redesnac.lsgbible.data.remote.dto.verse.Segond_1910
import org.redesnac.lsgbible.data.remote.dto.verse.VerseIndex
import org.redesnac.lsgbible.data.remote.dto.verse.Verses
import org.redesnac.lsgbible.domain.book.BookRepository
import org.redesnac.lsgbible.getDefaultBookDto
import org.redesnac.lsgbible.getDefaultVerseDto

class BookRepositoryImplTest{

    companion object {
        private val GET_ALL_BOOKS: List<BookDto> = listOf(getDefaultBookDto())
        private val RESULT = org.redesnac.lsgbible.data.remote.dto.verse.Result(
            chapterVerseRaw ="",
            nav = Nav(),
            bookRaw ="",
            versesCount= 20,
            chapterVerse = "",
            verseIndex = VerseIndex(listOf()),
            bookID = 0L,
            bookName = "",
            bookShort = "",
            verses = Verses(segond_1910 = Segond_1910(mapOf("1" to getDefaultVerseDto()))),
            singleVerse = true
        )
    }


    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    val api1: BibleApi1 = mockk()
    val api2: BibleApi2 = mockk()
    private val coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider()

    private val bookRepositoryImpl:BookRepository = BookRepositoryImpl(api1, api2, coroutineDispatcherProvider)

    @Before
    fun setUp() {
        coEvery { api1.fetchBooks() } returns BooksResponse(0L, listOf(getDefaultBookDto()), emptyList())
        coEvery { api2.fetchBook(any(), any()) } returns BookResponse(emptyList(), 0, emptyList(), "", emptyList(), listOf(RESULT), emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verifyBooks() = testCoroutineRule.runTest {
        // When
        val result = bookRepositoryImpl.fetchBooks()

        // Then
        assertk.assertThat(result).isEqualTo(GET_ALL_BOOKS)
        coVerify(exactly = 1) { api1.fetchBooks() }
        confirmVerified(api1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verifyBook() = testCoroutineRule.runTest {
        // When
        val result = bookRepositoryImpl.fetchBook("")

        // Then
        assertk.assertThat(result).isEqualTo(listOf(getDefaultVerseDto()))
        coVerify(exactly = 1) { api2.fetchBook(name = "") }
        confirmVerified(api2)
    }
}