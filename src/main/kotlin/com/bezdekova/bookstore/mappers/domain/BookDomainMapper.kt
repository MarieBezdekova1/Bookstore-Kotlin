package com.bezdekova.bookstore.mappers.domain

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.model.request.BookRequest
import com.bezdekova.bookstore.repositories.AuthorRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class BookDomainMapper(private val authorRepository: AuthorRepository) {

    fun map(request: BookRequest):Book {
        with(request) {
            val author = authorRepository.findById(authorId)
            return Book(
                    id = ObjectId(),
                    name = name,
                    price = price,
                    author = author
            )
        }
    }

}
