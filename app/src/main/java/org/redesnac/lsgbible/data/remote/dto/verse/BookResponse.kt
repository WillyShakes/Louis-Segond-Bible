package org.redesnac.lsgbible.data.remote.dto.verse

data class BookResponse(
    val disambiguation: List<Any>,
    val error_level: Int,
    val errors: List<Any>,
    val hash: String,
    val paging: List<Any>,
    val results: List<Result>,
    val strongs: List<Any>
)