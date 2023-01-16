package org.redesnac.lsgbible.domain.model.verse

import org.redesnac.lsgbible.domain.model.book.Book

data class VersesState(
    val isLoading:Boolean = false,
    val verses:List<Verse> = emptyList(),
    val error:String = "",
    val bookName:String = ""
)