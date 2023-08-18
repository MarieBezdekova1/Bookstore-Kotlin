package com.bezdekova.bookstore.constant

object MappingConstants {
    private const val API_V1 = "api/v1"

    const val AUTHORS = "$API_V1/authors"
    const val AUTHORS_ONLY = "$API_V1/authorsOnly"
    const val AUTHORS_ID = "$AUTHORS/{id}"

    const val BOOKS = "$API_V1/books"
    const val BOOKS_ID = "$BOOKS/{id}"
}
