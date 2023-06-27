package org.redesnac.lsgbible.domain.favorite_verse

import android.database.sqlite.SQLiteException
import org.redesnac.lsgbible.data.local.VerseDao
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertFavoriteVerseUseCase @Inject constructor(private val verseDao: VerseDao) {

    suspend fun invoke(verse: VerseDto): Boolean = try {
        verseDao.insert(verse)
        true
    } catch (e: SQLiteException) {
        e.printStackTrace()
        false
    }
}
