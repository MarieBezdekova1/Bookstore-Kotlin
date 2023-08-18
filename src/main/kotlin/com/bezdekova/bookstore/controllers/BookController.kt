package com.bezdekova.bookstore.controllers

import com.bezdekova.bookstore.constant.MappingConstants.BOOKS
import com.bezdekova.bookstore.constant.MappingConstants.BOOKS_ID
import com.bezdekova.bookstore.mappers.command.BookCommandMapper
import com.bezdekova.bookstore.mappers.response.BookResponseMapper
import com.bezdekova.bookstore.model.request.BookRequest
import com.bezdekova.bookstore.model.response.BookResponse
import com.bezdekova.bookstore.services.api.BookService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class BookController internal constructor(
        private val bookService: BookService,
        private val bookResponseMapper: BookResponseMapper,
        private val bookCommandMapper: BookCommandMapper
) {

    @GetMapping(BOOKS)
    @ResponseStatus(HttpStatus.OK)
    fun getAllBooks(): Page<BookResponse> {
        return bookService.findAll().map(bookResponseMapper::map)
    }

    @GetMapping(BOOKS_ID)
    @ResponseStatus(HttpStatus.OK)
    fun getBook(@PathVariable id: String): BookResponse? {
        return bookService.getBookById(id)
                .let(bookResponseMapper::map)
    }

    @PostMapping(BOOKS)
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(@RequestBody bookRequest: BookRequest): BookResponse? {
        return bookService.createBook(bookRequest)
                .let(bookResponseMapper::map)
    }

    @PutMapping(BOOKS_ID)
    @ResponseStatus(HttpStatus.OK)
    fun updateBook(@PathVariable id: String, @RequestBody bookRequest: BookRequest): BookResponse? {
        return bookCommandMapper.map(id = id, request = bookRequest)
                .run(bookService::updateBook)
                .let(bookResponseMapper::map)
    }

    @DeleteMapping(BOOKS_ID)
    @ResponseStatus(HttpStatus.OK)
    fun deleteBook(@PathVariable id: String) {
        return bookService.deleteBookById(id)
    }

}
