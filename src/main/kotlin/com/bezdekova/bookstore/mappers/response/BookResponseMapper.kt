package com.bezdekova.bookstore.mappers.response

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.model.response.BookResponse
import org.springframework.stereotype.Component

@Component
class BookResponseMapper(private val authorResponseMapper: AuthorResponseMapper) {

    fun map(book: Book): BookResponse = with(book) {
        BookResponse(
                name = name,
                price = price,
                author = author?.let { authorResponseMapper.map(it) }
        )
    }
}
