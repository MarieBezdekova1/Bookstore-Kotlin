package com.bezdekova.bookstore.model.response

@JvmRecord
data class AuthorWithBooksResponse(
        val name: String,
        val books: List<BookShortResponse>?
)
