package org.redesnac.lsgbible.domain.favorite_verse

import org.redesnac.lsgbible.data.BuildConfigResolver
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import org.redesnac.lsgbible.data.utils.Constants.BOOKS
import org.redesnac.lsgbible.data.utils.Constants.NUMBERS
import org.redesnac.lsgbible.data.utils.Constants.TEXTS
import javax.inject.Inject

class InsertRandomFavoriteVerseUseCase @Inject constructor(
    private val insertFavoriteVerseUseCase: InsertFavoriteVerseUseCase,
    private val buildConfigResolver: BuildConfigResolver,
) {


    suspend operator fun invoke() {
        if (buildConfigResolver.isDebug) {
            val randomBook = BOOKS.random()
            val randomChapter = NUMBERS.random()
            val randomVerse = NUMBERS.random()
            val randomText = TEXTS.random()
            insertFavoriteVerseUseCase.invoke(
                VerseDto(
                    book = randomBook,
                    chapter = randomChapter,
                    verse = randomVerse,
                    text = randomText,
                )
            )
        }
    }
}
