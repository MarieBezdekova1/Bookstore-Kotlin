package com.bezdekova.bookstore.services.api

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.model.command.BookUpdateCommand
import com.bezdekova.bookstore.model.request.BookRequest
import org.springframework.data.domain.Page
import org.springframework.web.multipart.MultipartFile

interface BookService {
    fun findAll(): Page<Book>
    fun getBookById(id: String): Book
    fun createBook(bookRequest: BookRequest): Book
    fun updateBook(command: BookUpdateCommand): Book
    fun deleteBookById(id: String)
    fun importBooksFromCsv(file: MultipartFile)
}
