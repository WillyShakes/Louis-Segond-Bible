package org.redesnac.lsgbible.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.redesnac.lsgbible.domain.model.verse.VersesState
import org.redesnac.lsgbible.domain.repository.BookRepository
import javax.inject.Inject


@HiltViewModel
class VersesViewModel @Inject constructor(
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle
    ):ViewModel(){

    private val tag: String = "VersesViewModel"

    private val _state = mutableStateOf(VersesState())
    val readOnlyState:State<VersesState> = _state

    init {
        Log.d(tag,"fetchVerses from init")
        savedStateHandle.get<String>("bookName")?.let { bookName ->
            if(bookName.isNotBlank())
            {
                viewModelScope.launch {
                    getVerses(bookName)
                }
            }
        }
    }


    private fun getVerses(name: String){
        viewModelScope.launch {
            Log.d(tag,"coroutine scope")
            try {
                Log.d(tag,"Loading")
                _state.value = VersesState(isLoading = true, bookName = name)
                val verses = repository.fetchBook(name).map{ it.toVerse() }
                Log.d(tag,"result $verses")
                _state.value = VersesState( verses = verses, bookName = name)
            } catch (e:java.lang.Exception){
                Log.d(tag,"Error ${e.message.toString()}")
                _state.value = VersesState(error = e.message.toString(), bookName = name)
            }
        }
    }



}