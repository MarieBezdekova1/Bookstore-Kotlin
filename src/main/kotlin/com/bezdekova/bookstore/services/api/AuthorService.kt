package com.bezdekova.bookstore.services.api

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.bezdekova.bookstore.model.request.AuthorRequest

interface AuthorService {
    val allAuthors: List<Author>
    fun getAuthorById(id: String): Author?
    fun createAuthor(authorRequest: AuthorRequest): Author?
    fun updateAuthor(command: AuthorUpdateCommand): Author
    fun deleteAuthor(id: String)
}
