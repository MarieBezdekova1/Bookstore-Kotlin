package com.bezdekova.bookstore.services.api

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.model.response.AuthorWithBooksResponse
import org.springframework.data.domain.Page
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody

interface AuthorService {
    fun findAll(): Page<Author>
    fun findAllAuthorsWithBooks(): List<AuthorWithBooksResponse>
    fun getAuthorById(id: String): Author
    fun createAuthor(authorRequest: AuthorRequest): Author
    fun updateAuthor(command: AuthorUpdateCommand): Author
    fun deleteAuthorById(id: String)
    fun importAuthorsFromCsv(file: MultipartFile)
    fun exportAuthorsToCsv(): StreamingResponseBody
}
