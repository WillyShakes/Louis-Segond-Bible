package org.redesnac.lsgbible.domain.favorite_verse

import org.redesnac.lsgbible.data.local.VerseDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteFavoriteVerseUseCase @Inject constructor(private val verseDao: VerseDao) {
    suspend fun invoke(verseId: String) {
        verseDao.delete(verseId)
    }
}
