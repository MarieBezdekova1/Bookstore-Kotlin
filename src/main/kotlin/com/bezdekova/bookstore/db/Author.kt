package com.bezdekova.bookstore.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Author(id: String, name: String) {
    @Id
    lateinit var id: String
    lateinit var name: String

}
