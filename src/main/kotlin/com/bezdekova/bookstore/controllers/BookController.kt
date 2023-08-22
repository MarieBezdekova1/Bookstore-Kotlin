package com.bezdekova.bookstore.controllers

import com.bezdekova.bookstore.constant.MappingConstants.BOOKS
import com.bezdekova.bookstore.constant.MappingConstants.BOOKS_ID
import com.bezdekova.bookstore.constant.MappingConstants.IMPORT_BOOKS
import com.bezdekova.bookstore.constant.MappingConstants.IMPORT_BOOKS_DEFAULT
import com.bezdekova.bookstore.constant.MappingConstants.REGISTER_BOOK
import com.bezdekova.bookstore.mappers.command.BookCommandMapper
import com.bezdekova.bookstore.mappers.response.BookResponseMapper
import com.bezdekova.bookstore.model.request.BookRequest
import com.bezdekova.bookstore.model.response.BookResponse
import com.bezdekova.bookstore.properties.CSVImportProperties
import com.bezdekova.bookstore.services.api.BookService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@SecurityRequirement(name = "securityschema")
class BookController internal constructor(
        private val bookService: BookService,
        private val bookResponseMapper: BookResponseMapper,
        private val bookCommandMapper: BookCommandMapper,
        private val csvImportProperties: CSVImportProperties
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

    @PostMapping(REGISTER_BOOK)
    @ResponseStatus(HttpStatus.OK)
    fun addNewBookWithRabbitMQ(@RequestBody bookRequest: BookRequest): String? {
        return bookService.addNewBook(bookRequest)
    }

    @PostMapping(IMPORT_BOOKS)
    @ResponseStatus(HttpStatus.OK)
    // tady zkus spíše poslat soubor přes API - multipart file
    fun importBooks(@RequestParam filePath: String) {
        bookService.importBooksFromCsv(filePath)
    }

    // toto se dá udělat pomocí configurace a injectu do constructoru - mrkni na directory properties + annotaci nad BookstoreApplication
    @Value("\${csv.books-file-path}")
    private lateinit var booksCsvFile: String

    @PostMapping(IMPORT_BOOKS_DEFAULT)
    @ResponseStatus(HttpStatus.OK)
    fun importBooksDefault() {
        bookService.importBooksFromCsv(csvImportProperties.booksFilePath)
    }

}
