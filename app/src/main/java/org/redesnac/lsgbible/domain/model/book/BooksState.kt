package org.redesnac.lsgbible.domain.model.book

data class BooksState(
    val isLoading:Boolean = false,
    val books:List<Book> = emptyList(),
    val error:String = "",
    val book: Book? = null
)
