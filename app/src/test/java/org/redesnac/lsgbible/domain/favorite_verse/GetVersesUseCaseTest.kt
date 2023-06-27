package org.redesnac.lsgbible.domain.favorite_verse

import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.redesnac.lsgbible.TestCoroutineRule
import org.redesnac.lsgbible.data.local.VerseDao
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import org.redesnac.lsgbible.getDefaultVersesDto

class GetVersesUseCaseTest{

    companion object {
        private val GET_ALL_VERSES: Flow<List<VerseDto>> = flowOf(getDefaultVersesDto(5))
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val verseDao: VerseDao = mockk()

    private val getVersesUseCase: GetVersesUseCase = GetVersesUseCase(verseDao)

    @Before
    fun setUp() {
        coEvery { verseDao.getAllVerses() } returns GET_ALL_VERSES
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verifyVerses() = testCoroutineRule.runTest {
        // When
        val result = getVersesUseCase.invoke()

        // Then
        assertk.assertThat(result).isEqualTo(GET_ALL_VERSES)
        coVerify(exactly = 1) { verseDao.getAllVerses() }
        confirmVerified(verseDao)
    }




}