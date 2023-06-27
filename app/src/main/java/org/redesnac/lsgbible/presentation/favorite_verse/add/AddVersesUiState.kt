package org.redesnac.lsgbible.presentation.favorite_verse.add

data class AddVersesUiState(
    val isLoading:Boolean = false,
    val isOkButtonVisible: Boolean = true,
    val event: AddVerseEvent = AddVerseEvent.Nothing
)