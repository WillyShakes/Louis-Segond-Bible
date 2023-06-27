package org.redesnac.lsgbible.domain.favorite_verse

import kotlinx.coroutines.flow.Flow
import org.redesnac.lsgbible.data.local.VerseDao
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetVersesUseCase @Inject constructor(
    private val verseDao: VerseDao,
) {
    operator fun invoke(): Flow<List<VerseDto>> = verseDao.getAllVerses()
}
