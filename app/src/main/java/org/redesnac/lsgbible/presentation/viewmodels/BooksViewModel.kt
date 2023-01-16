package org.redesnac.lsgbible.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.redesnac.lsgbible.domain.model.book.Book
import org.redesnac.lsgbible.domain.model.book.BooksState
import org.redesnac.lsgbible.domain.repository.BookRepository
import javax.inject.Inject


@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BookRepository
    ):ViewModel(){

    private val tag: String = "BooksViewModel"

    private val _state = mutableStateOf(BooksState())
    val readOnlyState:State<BooksState> = _state

    init {
        Log.d(tag,"fetchBooks from init")
        getBooks()
    }


    private fun getBooks(){
        viewModelScope.launch {
            Log.d(tag,"coroutine scope")
            try {
                Log.d(tag,"Loading")
                _state.value = BooksState(isLoading = true)
                val books = repository.fetchBooks().map{ it.toBook() }
                Log.d(tag,"result $books")
                _state.value = BooksState( books = books)
            } catch (e:java.lang.Exception){
                Log.d(tag,"Error ${e.message.toString()}")
                _state.value = BooksState(error = e.message.toString())
            }
        }
    }


    fun onBookCliked(book: Book?){
        _state.value = BooksState(book = book, books = _state.value.books)
    }
}