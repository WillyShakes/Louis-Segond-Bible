package org.redesnac.lsgbible.domain.favorite_verse

import android.database.sqlite.SQLiteException
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.mockk.coEvery
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
import org.redesnac.lsgbible.getDefaultVerseDto

class InsertFavoriteVerseUseCaseTest{

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val verseDao: VerseDao = mockk()

    private val insertFavoriteVerseUseCase = InsertFavoriteVerseUseCase(verseDao)

    @Before
    fun setUp() {
        coJustRun { verseDao.insert(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = insertFavoriteVerseUseCase.invoke(getDefaultVerseDto())

        // Then
        assertk.assertThat(result).isTrue()
        coVerify(exactly = 1) { verseDao.insert(getDefaultVerseDto()) }
        confirmVerified(verseDao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `error case - SQLiteException is thrown`() = testCoroutineRule.runTest {
        // Given
        coEvery { insertFavoriteVerseUseCase.invoke(any()) } throws SQLiteException()

        // When
        val result = insertFavoriteVerseUseCase.invoke(getDefaultVerseDto())

        // Then
        assertk.assertThat(result).isFalse()
        coVerify(exactly = 1) { verseDao.insert(getDefaultVerseDto()) }
        confirmVerified(verseDao)
    }

}

