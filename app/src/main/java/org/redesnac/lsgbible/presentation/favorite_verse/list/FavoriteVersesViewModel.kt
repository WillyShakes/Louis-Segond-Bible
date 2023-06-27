package org.redesnac.lsgbible.presentation.favorite_verse.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.redesnac.lsgbible.domain.CoroutineDispatcherProvider
import org.redesnac.lsgbible.domain.favorite_verse.DeleteFavoriteVerseUseCase
import org.redesnac.lsgbible.domain.favorite_verse.GetVersesUseCase
import org.redesnac.lsgbible.domain.favorite_verse.InsertRandomFavoriteVerseUseCase
import org.redesnac.lsgbible.presentation.utils.EquatableCallback
import javax.inject.Inject


@HiltViewModel
class FavoriteVersesViewModel @Inject constructor(
    private val deleteFavoriteVerseUseCase: DeleteFavoriteVerseUseCase,
    private val insertRandomFavoriteVerseUseCase: InsertRandomFavoriteVerseUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val getFavoriteVerseUseCase: GetVersesUseCase
):ViewModel(){

    private val mutableState = MutableStateFlow<VerseViewState>(VerseViewState.nothing)
    val state = mutableState.asStateFlow()

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            getFavoriteVerses()
        }
    }

    private suspend fun getFavoriteVerses(){
        try {
            mutableState.value = VerseViewState.loading
            getFavoriteVerseUseCase.invoke().collect(){
                    verseDtos ->
                if(verseDtos.isEmpty())
                {
                    mutableState.value = VerseViewState.onVerseLoaded(listOf(VerseViewStateItem.EmptyState))
                }
                else
                {
                    val verses: List<VerseViewStateItem.Verse> = verseDtos.map {
                            verseDto ->
                        VerseViewStateItem.Verse(
                            verseId = verseDto.id,
                            chapter = verseDto.chapter,
                            verseNumber = verseDto.verse,
                            onDeleteEvent = EquatableCallback {
                                viewModelScope.launch(coroutineDispatcherProvider.io) {
                                    deleteFavoriteVerseUseCase.invoke(verseDto.id)
                                }
                            },
                        )
                    }
                    mutableState.value = VerseViewState.onVerseLoaded(
                        list = verses
                    )
                }

            }
        } catch (e: Exception){
            mutableState.value = VerseViewState.toast(e.message.toString())
        }
    }

    fun onAddButtonLongClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            insertRandomFavoriteVerseUseCase.invoke()
        }
    }
}