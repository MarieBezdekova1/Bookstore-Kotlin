package com.bezdekova.bookstore.model.response

@JvmRecord
data class BookResponse(
        val name: String,
        val price: Int?,
        val author: AuthorResponse?
)
