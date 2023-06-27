package org.redesnac.lsgbible.presentation.book

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.redesnac.lsgbible.TestCoroutineRule
import org.redesnac.lsgbible.domain.book.BookRepository
import org.redesnac.lsgbible.getDefaultBooks
import org.redesnac.lsgbible.getDefaultBooksDto

class BooksViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val repository: BookRepository = mockk()
    private val booksViewModel = BooksViewModel(
        repository,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),)

    @Before
    fun setUp() {
        coEvery { repository.fetchBooks() } returns getDefaultBooksDto()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nominal case list`() = testCoroutineRule.runTest {
        runCurrent()
        // When
        assertk.assertThat(booksViewModel.readOnlyState.value).isEqualTo(getDefaultBooksState().copy(
            books = getDefaultBooks(), isLoading = false
        ))
    }

    private fun getDefaultBooksState(): BooksState {
        return BooksState(isLoading = true)
    }

}