package com.bezdekova.bookstore.mappers.response

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.model.response.BookShortResponse
import org.springframework.stereotype.Component

@Component
class BookShortResponseMapper {

    fun map(book: Book): BookShortResponse = with(book) {
        BookShortResponse(
                name = name,
                price = price
        )
    }
}
