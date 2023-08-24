package com.bezdekova.bookstore.mappers.csv

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.repositories.AuthorRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class BookCsvMapper(private val authorRepository: AuthorRepository) {

    fun map(csvLine: Array<String>): Book {
        val author = csvLine[2].let { authorRepository.findById(it) }
        return Book(
                id = ObjectId(),
                name = csvLine[0],
                price = csvLine[1].toIntOrNull(),
                author = author
        )
    }
}
