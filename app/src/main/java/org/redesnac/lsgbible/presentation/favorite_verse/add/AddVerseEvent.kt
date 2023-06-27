package org.redesnac.lsgbible.presentation.favorite_verse.add

sealed interface AddVerseEvent {
    object Dismiss : AddVerseEvent
    data class Toast(val text: String) : AddVerseEvent
    object Nothing : AddVerseEvent
}