package org.redesnac.lsgbible.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.redesnac.lsgbible.domain.CoroutineDispatcherProvider
import org.redesnac.lsgbible.domain.book.Book
import org.redesnac.lsgbible.domain.book.BookRepository
import javax.inject.Inject


@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BookRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    ):ViewModel(){

    private val _state = MutableStateFlow<BooksState>(BooksState())
    val readOnlyState = _state.asStateFlow()

    init {
        getBooks()
    }


    private fun getBooks(){
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            try {
                _state.update { currentState ->
                    currentState.copy(isLoading = true)
                }
                val books = repository.fetchBooks().map{ it.toBook() }
                withContext(coroutineDispatcherProvider.main) {
                    _state.update { currentState ->
                        currentState.copy(books = books, isLoading = false)
                    }
                }
            } catch (e:java.lang.Exception){
                withContext(coroutineDispatcherProvider.main) {
                    _state.update { currentState ->
                        currentState.copy(error = e.message.toString(), isLoading = false)
                    }
                }
            }
        }
    }


    fun onBookCliked(book: Book?){
        _state.value = BooksState(book = book, books = _state.value.books)
    }
}