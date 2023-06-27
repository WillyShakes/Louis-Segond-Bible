package org.redesnac.lsgbible.domain.favorite_verse

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.redesnac.lsgbible.TestCoroutineRule
import org.redesnac.lsgbible.data.BuildConfigResolver
import org.redesnac.lsgbible.data.utils.Constants.BOOKS
import org.redesnac.lsgbible.data.utils.Constants.NUMBERS
import org.redesnac.lsgbible.data.utils.Constants.TEXTS

class InsertRandomFavoriteVerseUseCaseTest{




    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val insertFavoriteVerseUseCase: InsertFavoriteVerseUseCase = mockk()
    private val buildConfigResolver: BuildConfigResolver = mockk()

    private val insertRandomFavoriteVerseUseCase = InsertRandomFavoriteVerseUseCase(insertFavoriteVerseUseCase, buildConfigResolver)

    @Before
    fun setUp() {
        coEvery { insertFavoriteVerseUseCase.invoke(any()) }  returns true
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nominal case - debug`() = testCoroutineRule.runTest {
        // Given
        every { buildConfigResolver.isDebug } returns true

        // When
        insertRandomFavoriteVerseUseCase.invoke()

        // Then
        coVerify(exactly = 1) {
            insertFavoriteVerseUseCase.invoke(
                match { verseDto ->
                    NUMBERS.contains(verseDto.verse)
                            && BOOKS.contains(verseDto.book)
                            && NUMBERS.contains(verseDto.chapter)
                            && TEXTS.contains(verseDto.text)
                }
            )
            buildConfigResolver.isDebug
        }
        confirmVerified(insertFavoriteVerseUseCase, buildConfigResolver)
    }


}