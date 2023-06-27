package org.redesnac.lsgbible.data.local

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import assertk.assertions.isEqualTo
import com.google.gson.Gson
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.redesnac.lsgbible.TestCoroutineRule
import org.redesnac.lsgbible.domain.favorite_verse.InsertFavoriteVerseUseCase
import org.redesnac.lsgbible.getDefaultVerseDto
import org.redesnac.lsgbible.getDefaultVerseDtoAsJson

class InitializeDatabaseWorkerTest{
    companion object {
        private const val KEY_INPUT_DATA = "KEY_INPUT_DATA"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val context: Context = mockk()
    private val workerParams: WorkerParameters = mockk(relaxed = true)
    private val insertFavoriteVerseUseCase: InsertFavoriteVerseUseCase = mockk()
    private val gson: Gson = DataModule().provideGson()


    private val initializeDatabaseWorker = spyk(
        InitializeDatabaseWorker(
            context = context,
            workerParams = workerParams,
            insertFavoriteVerseUseCase = insertFavoriteVerseUseCase,
            gson = gson,
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider()
        )
    )

    @Before
    fun setUp() {
        every { initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA) } returns getDefaultVerseDtoAsJson()
        coJustRun { insertFavoriteVerseUseCase.invoke(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = initializeDatabaseWorker.doWork()

        // Then
        assertk.assertThat(result).isEqualTo(ListenableWorker.Result.success())
        coVerify(exactly = 1) {
            initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA)
            insertFavoriteVerseUseCase.invoke(getDefaultVerseDto())
        }
        confirmVerified(insertFavoriteVerseUseCase)
    }


}