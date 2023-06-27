package org.redesnac.lsgbible.domain.favorite_verse

import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.redesnac.lsgbible.TestCoroutineRule
import org.redesnac.lsgbible.data.local.VerseDao

class DeleteFavoriteVerseUseCaseTest{

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val verseDao: VerseDao = mockk()

    private val deleteFavoriteVerseUseCase = DeleteFavoriteVerseUseCase(verseDao)


    @Before
    fun setUp() {
        coJustRun { verseDao.delete(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // Given
        val verseId = "id"

        //When
        deleteFavoriteVerseUseCase.invoke(verseId)

        // Then
        coVerify(exactly = 1) { verseDao.delete(verseId = verseId) }
        confirmVerified(verseDao)
    }

}