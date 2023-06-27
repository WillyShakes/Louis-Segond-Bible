package org.redesnac.lsgbible.presentation.favorite_verse.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.redesnac.lsgbible.R
import org.redesnac.lsgbible.TestCoroutineRule
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import org.redesnac.lsgbible.domain.favorite_verse.InsertFavoriteVerseUseCase
import org.redesnac.lsgbible.presentation.utils.NativeText
import java.util.concurrent.CountDownLatch

class AddFavoriteVerseViewModelTest {

    companion object {
        private const val INSERT_TASK_DELAY = 50L

        private const val DEFAULT_VERSE = "1"
        private const val DEFAULT_CHAPTER = "1"
        private const val DEFAULT_BOOK = "génèse"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    private val insertFavoriteVerseUseCase: InsertFavoriteVerseUseCase = mockk()

    private val addFavoriteVerseViewModel = AddFavoriteVerseViewModel(
        insertFavoriteVerseUseCase = insertFavoriteVerseUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        coEvery { insertFavoriteVerseUseCase.invoke(any()) } coAnswers {
            delay(INSERT_TASK_DELAY)
            true
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // When
        val state = addFavoriteVerseViewModel.readOnlyUiState
        // Then
        assertThat(state.value).isEqualTo(getDefaultFavoriteVerseState())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // Given
        addFavoriteVerseViewModel.onVerseSelected(DEFAULT_VERSE)
        addFavoriteVerseViewModel.onChapterSelected(DEFAULT_CHAPTER)
        addFavoriteVerseViewModel.onBookSelected(DEFAULT_BOOK)
        addFavoriteVerseViewModel.onOkButtonClicked()
        var latch = CountDownLatch(1)
        // When
        val job = async(Dispatchers.IO) {
            addFavoriteVerseViewModel.readOnlyUiState.collect { state ->
                // Then
                assertThat(state).isEqualTo(getDefaultFavoriteVerseState().copy(
                    isLoading = true,
                    isOkButtonVisible = false
                )
                )
                latch.countDown()
            }
        }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()

        // When 2
        advanceTimeBy(INSERT_TASK_DELAY)
        runCurrent()

        // Then 2
        latch = CountDownLatch(1)
        val job1 = async(Dispatchers.IO) {
            addFavoriteVerseViewModel.readOnlyUiState.collect { state ->
                // Then
                assertThat(state).isEqualTo(getDefaultFavoriteVerseState().copy(
                    event = AddVerseEvent.Dismiss
                )
                )
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job1.cancelAndJoin()

        coVerify(exactly = 1) {
            insertFavoriteVerseUseCase.invoke(
                VerseDto(
                    book = DEFAULT_BOOK,
                    chapter = DEFAULT_CHAPTER,
                    verse = DEFAULT_VERSE,
                    text = ""
                )
            )
        }
        confirmVerified(insertFavoriteVerseUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `error case - can't insert verse in database`() = testCoroutineRule.runTest {
        // Given
        coEvery { insertFavoriteVerseUseCase.invoke(any()) } returns false
        addFavoriteVerseViewModel.onVerseSelected(DEFAULT_VERSE)
        addFavoriteVerseViewModel.onChapterSelected(DEFAULT_CHAPTER)
        addFavoriteVerseViewModel.onBookSelected(DEFAULT_BOOK)
        addFavoriteVerseViewModel.onOkButtonClicked()

        advanceTimeBy(INSERT_TASK_DELAY)
        runCurrent()

        // When
        val latch = CountDownLatch(1)
        val job1 = async(Dispatchers.IO) {
            addFavoriteVerseViewModel.readOnlyUiState.collect { state ->
                // Then
                assertThat(state).isEqualTo(getDefaultFavoriteVerseState().copy(
                    event = AddVerseEvent.Toast(NativeText.Resource(R.string.cant_insert_verse).toString())
                )
                )
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job1.cancelAndJoin()

        coVerify(exactly = 1) {
            insertFavoriteVerseUseCase.invoke(
                VerseDto(
                    book = DEFAULT_BOOK,
                    chapter = DEFAULT_CHAPTER,
                    verse = DEFAULT_VERSE,
                    text = ""
                )
            )
        }
        confirmVerified(insertFavoriteVerseUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `edge case - user didn't specify book`() = testCoroutineRule.runTest {
        // When
        addFavoriteVerseViewModel.onOkButtonClicked()

        // Then
        advanceTimeBy(INSERT_TASK_DELAY)
        runCurrent()

        // When
        val latch = CountDownLatch(1)
        val job1 = async(Dispatchers.IO) {
            addFavoriteVerseViewModel.readOnlyUiState.collect { state ->
                // Then
                assertThat(state).isEqualTo(getDefaultFavoriteVerseState().copy(
                    event = AddVerseEvent.Toast(NativeText.Resource(R.string.error_insert_verse).toString())
                )
                )
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job1.cancelAndJoin()
        coVerify(exactly = 0) { insertFavoriteVerseUseCase.invoke(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `edge case - user didn't specify chapter`() = testCoroutineRule.runTest {
        // When
        addFavoriteVerseViewModel.onBookSelected(DEFAULT_BOOK)
        addFavoriteVerseViewModel.onOkButtonClicked()

        // Then
        advanceTimeBy(INSERT_TASK_DELAY)
        runCurrent()

        // When
        val latch = CountDownLatch(1)
        val job1 = async(Dispatchers.IO) {
            addFavoriteVerseViewModel.readOnlyUiState.collect { state ->
                // Then
                assertThat(state).isEqualTo(getDefaultFavoriteVerseState().copy(
                    event = AddVerseEvent.Toast(NativeText.Resource(R.string.error_insert_verse).toString())
                )
                )
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job1.cancelAndJoin()
        coVerify(exactly = 0) { insertFavoriteVerseUseCase.invoke(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `edge case - user didn't specify verse`() = testCoroutineRule.runTest {
        // When
        addFavoriteVerseViewModel.onBookSelected(DEFAULT_BOOK)
        addFavoriteVerseViewModel.onChapterSelected(DEFAULT_CHAPTER)
        addFavoriteVerseViewModel.onOkButtonClicked()

        // Then
        advanceTimeBy(INSERT_TASK_DELAY)
        runCurrent()

        // When
        val latch = CountDownLatch(1)
        val job1 = async(Dispatchers.IO) {
            addFavoriteVerseViewModel.readOnlyUiState.collect { state ->
                // Then
                assertThat(state).isEqualTo(getDefaultFavoriteVerseState().copy(
                    event = AddVerseEvent.Toast(NativeText.Resource(R.string.error_insert_verse).toString())
                )
                )
                latch.countDown()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job1.cancelAndJoin()
        coVerify(exactly = 0) { insertFavoriteVerseUseCase.invoke(any()) }
    }

    // region OUT
    private fun getDefaultFavoriteVerseState() = AddVersesUiState()

}