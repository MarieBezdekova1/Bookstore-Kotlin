package com.bezdekova.bookstore.mappers.command

import com.bezdekova.bookstore.model.command.BookUpdateCommand
import com.bezdekova.bookstore.model.request.BookRequest
import org.springframework.stereotype.Component

@Component
class BookCommandMapper {

    fun map(id: String, request: BookRequest) = with(request) {
        BookUpdateCommand(
                id = id,
                name = name,
                price = price,
                authorId = authorId
        )
    }
}