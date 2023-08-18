package com.bezdekova.bookstore.model.response

@JvmRecord
data class BookShortResponse(
        val name: String,
        val price: Int?
)
