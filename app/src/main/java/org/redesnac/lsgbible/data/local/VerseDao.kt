package org.redesnac.lsgbible.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto

@Dao
public interface VerseDao {
    @Insert
    suspend fun insert(verseDto: VerseDto)

    @Query("SELECT * FROM verse")
    @Transaction
    fun getAllVerses(): Flow<List<VerseDto>>

    @Query("DELETE FROM verse WHERE id=:verseId")
    suspend fun delete(verseId: String): Int
}