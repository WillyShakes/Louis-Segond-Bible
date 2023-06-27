package org.redesnac.lsgbible.presentation.favorite_verse.add

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.redesnac.lsgbible.R
import org.redesnac.lsgbible.data.remote.dto.verse.VerseDto
import org.redesnac.lsgbible.domain.CoroutineDispatcherProvider
import org.redesnac.lsgbible.domain.favorite_verse.InsertFavoriteVerseUseCase
import org.redesnac.lsgbible.presentation.utils.NativeText
import javax.inject.Inject


@SuppressLint("LongLogTag")
@HiltViewModel
class AddFavoriteVerseViewModel @Inject constructor(
    private val insertFavoriteVerseUseCase: InsertFavoriteVerseUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
):ViewModel(){
    private val _uiState = MutableStateFlow(AddVersesUiState())
    val readOnlyUiState: StateFlow<AddVersesUiState> = _uiState.asStateFlow()

    var book by mutableStateOf("")
        private set
    var chapter by mutableStateOf("")
        private set
    var verse by mutableStateOf("")
        private set

    fun onBookSelected(book: String) {
        this.book = book
    }

    fun onChapterSelected(chapter: String) {
        this.chapter = chapter
    }

    fun onVerseSelected(verse: String) {
        this.verse = verse
    }

    fun onOkButtonClicked() {
        val capturedBook = book
        val capturedChapter = chapter
        val capturedVerse = verse

        if (capturedBook.isNotBlank() && capturedChapter.isNotBlank() && capturedVerse.isNotBlank()) {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, isOkButtonVisible = false)
            }

            viewModelScope.launch(coroutineDispatcherProvider.io) {
                val success = insertFavoriteVerseUseCase.invoke(
                    VerseDto(
                        book = book,
                        chapter = chapter,
                        verse = verse,
                        text = ""//TODO invoke API to get text
                    )
                )

                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, isOkButtonVisible = true)
                }

                withContext(coroutineDispatcherProvider.main) {
                    val event: AddVerseEvent
                    if (success) {
                        event = AddVerseEvent.Dismiss
                    } else {
                        event = AddVerseEvent.Toast(NativeText.Resource(R.string.cant_insert_verse).toString())
                    }
                    _uiState.update { currentState ->
                        currentState.copy(event = event)
                    }
                }
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(event = AddVerseEvent.Toast(NativeText.Resource(R.string.error_insert_verse).toString()))
            }
        }
    }
}