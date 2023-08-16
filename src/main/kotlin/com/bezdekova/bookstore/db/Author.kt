package com.bezdekova.bookstore.db

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Author(
        @Id
        // U Monga by se mělo používat ObjectId, přidal jsem do importů
        val id: String,
        var name: String
)
// prázdné závorky v kotlinu psát nemusíš
