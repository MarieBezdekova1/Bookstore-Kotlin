package com.bezdekova.bookstore.mappers.csv

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.repositories.AuthorRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class BookCsvMapper(private val authorRepository: AuthorRepository) {

    fun map(csvLine: Array<String>): Book? {
        val author = csvLine.getOrNull(2)?.let { authorRepository.findById(it) }
        return csvLine.getOrNull(0)?.run {
            Book(
                id = ObjectId(),
                name = csvLine[0],
                price = csvLine.getOrNull(1)?.toIntOrNull(),
                author = author
            )
        }
    }
}
