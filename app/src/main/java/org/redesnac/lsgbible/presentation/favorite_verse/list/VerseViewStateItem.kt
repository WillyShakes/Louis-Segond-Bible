package org.redesnac.lsgbible.presentation.favorite_verse.list

import org.redesnac.lsgbible.presentation.utils.EquatableCallback

sealed class VerseViewStateItem(
    val type: Type,
) {
    enum class Type {
        HEADER,
        VERSE,
        EMPTY_STATE,
    }

    data class Header(
        val title: String,
    ) : VerseViewStateItem(Type.HEADER)

    data class Verse(
        val verseId: String,
        val chapter: String,
        val verseNumber: String,
        val onDeleteEvent: EquatableCallback,
    ) : VerseViewStateItem(Type.VERSE)

    object EmptyState : VerseViewStateItem(Type.EMPTY_STATE)
}

sealed class VerseViewState {
    object nothing : VerseViewState()
    object loading : VerseViewState()
    data class toast(val text: String) : VerseViewState()
    data class onVerseLoaded(val list: List<VerseViewStateItem>) : VerseViewState()
}