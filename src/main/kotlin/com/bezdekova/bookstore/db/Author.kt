package com.bezdekova.bookstore.db

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Author(
        @Id
        val id: ObjectId,
        var name: String)
