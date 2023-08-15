package com.bezdekova.bookstore.mappers.response

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.response.AuthorResponse
import org.springframework.stereotype.Component

@Component
class AuthorResponseMapper() {

    fun map(author: Author?): AuthorResponse? {
        if (author != null) {
            return AuthorResponse(author.id, author.name)
        }
        return null
    }
}
