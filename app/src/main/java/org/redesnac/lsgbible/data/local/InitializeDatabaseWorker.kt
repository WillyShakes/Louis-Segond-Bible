package org.redesnac.lsgbible.data.local

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import org.redesnac.lsgbible.data.utils.fromJson
import org.redesnac.lsgbible.domain.CoroutineDispatcherProvider
import org.redesnac.lsgbible.domain.favorite_verse.InsertFavoriteVerseUseCase

@HiltWorker
class InitializeDatabaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val insertFavoriteVerseUseCase: InsertFavoriteVerseUseCase,
    private val gson: Gson,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_INPUT_DATA = "KEY_INPUT_DATA"
    }

    override suspend fun doWork(): Result = withContext(coroutineDispatcherProvider.io) {
        val entitiesAsJson = inputData.getString(KEY_INPUT_DATA)

        if (entitiesAsJson != null) {
            val verseEntities = gson.fromJson<List<VerseDto>>(json = entitiesAsJson)

            if (verseEntities != null) {
                verseEntities.forEach { verse ->
                    insertFavoriteVerseUseCase.invoke(verse)
                }
                Result.success()
            } else {
                Log.e(javaClass.simpleName, "Gson can't parse verses : $entitiesAsJson")
                Result.failure()
            }
        } else {
            Log.e(javaClass.simpleName, "Failed to get data with key $KEY_INPUT_DATA from data: $inputData")
            Result.failure()
        }
    }
}