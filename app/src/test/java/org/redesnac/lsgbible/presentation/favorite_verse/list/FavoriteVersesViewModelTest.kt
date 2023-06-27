package org.redesnac.lsgbible.presentation.favorite_verse.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertions.isEqualTo
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.redesnac.lsgbible.TestCoroutineRule
import org.redesnac.lsgbible.domain.favorite_verse.DeleteFavoriteVerseUseCase
import org.redesnac.lsgbible.domain.favorite_verse.GetVersesUseCase
import org.redesnac.lsgbible.domain.favorite_verse.InsertRandomFavoriteVerseUseCase
import org.redesnac.lsgbible.getDefaultVersesDto
import org.redesnac.lsgbible.presentation.utils.EquatableCallback

class FavoriteVersesViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val deleteTaskUseCase: DeleteFavoriteVerseUseCase = mockk()
    private val insertRandomFavoriteVerseUseCase: InsertRandomFavoriteVerseUseCase = mockk()
    private val getVersesUseCase: GetVersesUseCase = mockk()

    private val favoriteVersesViewModel = FavoriteVersesViewModel(
        deleteFavoriteVerseUseCase = deleteTaskUseCase,
        insertRandomFavoriteVerseUseCase = insertRandomFavoriteVerseUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        getFavoriteVerseUseCase = getVersesUseCase
    )

    @Before
    fun setUp() {
        coJustRun { deleteTaskUseCase.invoke(any()) }
        coJustRun { insertRandomFavoriteVerseUseCase.invoke() }
        every { getVersesUseCase.invoke() } returns flowOf(getDefaultVersesDto())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nominal case loading`() = testCoroutineRule.runTest {
        // When
        favoriteVersesViewModel.state.first{ state ->
            // Then
            println(state)
            assertk.assertThat(state).isEqualTo(VerseViewState.loading)
            true
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nominal case list`() = testCoroutineRule.runTest {
        // When
        favoriteVersesViewModel.state.drop(1).first { state ->
            // Then
            println(state)
            assertk.assertThat(state).isEqualTo(getDefaultVerseState())
            true
        }
    }


    private fun getDefaultVerseState() = VerseViewState.onVerseLoaded(list = listOf(
        VerseViewStateItem.Verse(
            verseNumber = "38",
            chapter = "6",
            verseId = "1",
            onDeleteEvent = EquatableCallback {}
        ),
        VerseViewStateItem.Verse(
            verseNumber = "38",
            chapter = "6",
            verseId = "1",
            onDeleteEvent = EquatableCallback {}
        ),
        VerseViewStateItem.Verse(
            verseNumber = "38",
            chapter = "6",
            verseId = "1",
            onDeleteEvent = EquatableCallback {}
        )
    )
    )
}