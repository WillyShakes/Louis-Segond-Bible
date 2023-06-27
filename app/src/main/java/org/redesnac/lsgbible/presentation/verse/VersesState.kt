package org.redesnac.lsgbible.presentation.verse

import org.redesnac.lsgbible.domain.verse.Verse

data class VersesState(
    val isLoading:Boolean = false,
    val verses:List<Verse> = emptyList(),
    val error:String = "",
    val bookName:String = ""
)