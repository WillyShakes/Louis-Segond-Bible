package org.redesnac.lsgbible.presentation.book

import org.redesnac.lsgbible.domain.book.Book

data class BooksState(
    val isLoading:Boolean = false,
    val books:List<Book> = emptyList(),
    val error:String = "",
    val book: Book? = null
)
