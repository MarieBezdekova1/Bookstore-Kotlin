package com.bezdekova.bookstore.model.command

class BookUpdateCommand(
        val id: String,
        val name: String,
        val price: Int,
        val authorId: String
)
