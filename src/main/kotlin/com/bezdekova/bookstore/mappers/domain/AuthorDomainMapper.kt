package com.bezdekova.bookstore.mappers.domain

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.request.AuthorRequest
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class AuthorDomainMapper() {

    fun map(request: AuthorRequest) = with(request) {
        Author(
                id = ObjectId(),
                name = name
        )
    }

}
