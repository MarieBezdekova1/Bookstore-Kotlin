package com.bezdekova.bookstore.controllers

import com.bezdekova.bookstore.constant.MappingConstants.AUTHORS
import com.bezdekova.bookstore.constant.MappingConstants.AUTHORS_ID
import com.bezdekova.bookstore.constant.MappingConstants.AUTHORS_ONLY
import com.bezdekova.bookstore.constant.MappingConstants.IMPORT_AUTHORS
import com.bezdekova.bookstore.constant.MappingConstants.IMPORT_AUTHORS_DEFAULT
import com.bezdekova.bookstore.mappers.command.AuthorCommandMapper
import com.bezdekova.bookstore.mappers.response.AuthorResponseMapper
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.model.response.AuthorResponse
import com.bezdekova.bookstore.model.response.AuthorWithBooksResponse
import com.bezdekova.bookstore.services.api.AuthorService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@SecurityRequirement(name = "securityschema")
class AuthorController internal constructor(
        private val authorService: AuthorService,
        private val authorResponseMapper: AuthorResponseMapper,
        private val authorCommandMapper: AuthorCommandMapper
) {

    @GetMapping(AUTHORS_ONLY)
    @ResponseStatus(HttpStatus.OK)
    fun getAllAuthors(): Page<AuthorResponse> {
        return authorService.findAll().map(authorResponseMapper::map)
    }

    @GetMapping(AUTHORS)
    @ResponseStatus(HttpStatus.OK)
    fun getAllAuthorsWithBooks(): List<AuthorWithBooksResponse> {
        return authorService.findAllAuthorsWithBooks()
    }

    @GetMapping(AUTHORS_ID)
    @ResponseStatus(HttpStatus.OK)
    fun getAuthor(@PathVariable id: String): AuthorResponse? {
        return authorService.getAuthorById(id)
                .let(authorResponseMapper::map)
    }

    @PostMapping(AUTHORS)
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(@RequestBody authorRequest: AuthorRequest): AuthorResponse? {
        return authorService.createAuthor(authorRequest)
                .let(authorResponseMapper::map)
    }

    @PutMapping(AUTHORS_ID)
    @ResponseStatus(HttpStatus.OK)
    fun updateAuthor(@PathVariable id: String, @RequestBody authorRequest: AuthorRequest): AuthorResponse? {
        return authorCommandMapper.map(id = id, request = authorRequest)
                .run(authorService::updateAuthor)
                .let(authorResponseMapper::map)
    }

    @DeleteMapping(AUTHORS_ID)
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthor(@PathVariable id: String) {
        return authorService.deleteAuthorById(id)
    }

    @PostMapping(IMPORT_AUTHORS)
    @ResponseStatus(HttpStatus.OK)
    fun importAuthors(@RequestParam filePath: String) {
        authorService.importAuthorsFromCsv(filePath)
    }

    @Value("\${csv.authors-file-path}")
    private lateinit var authorsCsvFile: String

    @PostMapping(IMPORT_AUTHORS_DEFAULT)
    @ResponseStatus(HttpStatus.OK)
    fun importAuthorsDefault() {
        authorService.importAuthorsFromCsv(authorsCsvFile)
    }
}
