package com.bezdekova.bookstore.services.api

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.bezdekova.bookstore.model.request.AuthorRequest
import org.springframework.data.domain.Page

interface AuthorService {
    fun findAll(): Page<Author>
    fun getAuthorById(id: String): Author
    fun createAuthor(authorRequest: AuthorRequest): Author
    fun updateAuthor(command: AuthorUpdateCommand): Author
    fun deleteAuthorById(id: String)
}
