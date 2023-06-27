package org.redesnac.lsgbible

import org.redesnac.lsgbible.data.remote.dto.book.BookDto
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import org.redesnac.lsgbible.domain.book.Book
import org.redesnac.lsgbible.presentation.favorite_verse.list.VerseViewStateItem
import org.redesnac.lsgbible.presentation.utils.EquatableCallback

// region VerseDto
fun getDefaultVerseDto() = VerseDto(
    id ="1",
    book = "Luc",
    chapter = "6",
    verse = "38",
    text = "Donnez, et il vous sera donné: on versera dans votre sein une bonne mesure, serrée, secouée et qui déborde; car on vous mesurera avec la mesure dont vous vous serez servis."
)

fun getDefaultVerse() = VerseViewStateItem.Verse(
        verseNumber = "38",
        chapter = "6",
        verseId = "38",
        onDeleteEvent = EquatableCallback {}
    )


fun getDefaultVerseDtoAsJson() = """
    [
    {"id":"1", "book":"Luc","chapter":6,"verse":38,"text":"Donnez, et il vous sera donné: on versera dans votre sein une bonne mesure, serrée, secouée et qui déborde; car on vous mesurera avec la mesure dont vous vous serez servis."}
    ]
""".trimIndent()

fun getDefaultVersesDto(
    verseCount: Int = 3,
) = List(verseCount) {
            getDefaultVerseDto()
        }

// endregion VerseDto

//region book
fun getDefaultBookDto() = BookDto(
    chapters = 38,
    name = "Luc",
    id = 24,
    chapter_verses =  mapOf("1" to 4),
    shortname = "Lu"
)

fun getDefaultBook() = Book(
    chapters = 38,
    name = "Luc",
)

fun getDefaultBooks(
    bookCount: Int = 3,
) = List(bookCount) {
    getDefaultBook()
}

fun getDefaultBooksDto(
    bookCount: Int = 3,
) = List(bookCount) {
    getDefaultBookDto()
}

// endregion BookDto
