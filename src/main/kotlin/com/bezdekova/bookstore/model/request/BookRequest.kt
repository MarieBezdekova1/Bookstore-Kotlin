package com.bezdekova.bookstore.model.request

@JvmRecord
data class BookRequest(
        val name: String,
        val price: Int?,
        val authorId: String?
)
