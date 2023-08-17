package com.bezdekova.bookstore.mappers.command

import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.bezdekova.bookstore.model.request.AuthorRequest
import org.springframework.stereotype.Component

@Component
class AuthorCommandMapper {

    fun map(id: String, request: AuthorRequest) = with(request) {
        AuthorUpdateCommand(
                id = id,
                name = name
        )
    }
}