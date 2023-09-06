package com.bezdekova.bookstore.constant

object MappingConstants {
    private const val API_V1 = "api/v1"

    const val AUTHORS = "$API_V1/authors"
    const val AUTHORS_ONLY = "$API_V1/authorsOnly"
    const val AUTHORS_ID = "$AUTHORS/{id}"

    const val BOOKS = "$API_V1/books"
    const val BOOKS_ID = "$BOOKS/{id}"

    const val REGISTER_BOOK = "$API_V1/registerBook"

    const val IMPORT_AUTHORS = "$API_V1/import/authors"
    const val IMPORT_AUTHORS_DEFAULT = "$IMPORT_AUTHORS/default"

    const val IMPORT_BOOKS = "$API_V1/import/books"
    const val IMPORT_BOOKS_DEFAULT = "$IMPORT_BOOKS/default"

    const val EXPORT_AUTHORS = "$API_V1/export/authors"
    const val EXPORT_AUTHORS2 = "$API_V1/export/authors2"
    const val EXPORT_BOOKS = "$API_V1/export/books"
}
