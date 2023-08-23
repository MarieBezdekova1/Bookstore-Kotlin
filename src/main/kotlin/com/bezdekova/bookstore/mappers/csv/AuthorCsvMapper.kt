package com.bezdekova.bookstore.mappers.csv

import com.bezdekova.bookstore.db.Author
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class AuthorCsvMapper {

    fun map(csvLine: Array<String>) = Author(
            id = ObjectId(),
            name = csvLine[0]
    )
}
