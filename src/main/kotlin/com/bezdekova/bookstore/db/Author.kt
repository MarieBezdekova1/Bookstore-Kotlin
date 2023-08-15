package com.bezdekova.bookstore.db

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Author(
        @Id
        val id: String,
        var name: String) {
}
