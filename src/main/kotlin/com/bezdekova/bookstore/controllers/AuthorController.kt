package com.bezdekova.bookstore.controllers

import com.bezdekova.bookstore.constant.MappingConstants
import com.bezdekova.bookstore.mappers.command.AuthorCommandMapper
import com.bezdekova.bookstore.mappers.response.AuthorResponseMapper
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.model.response.AuthorResponse
import com.bezdekova.bookstore.services.api.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class AuthorController internal constructor(
        private val authorService: AuthorService,
        private val authorResponseMapper: AuthorResponseMapper,
        private val authorCommandMapper: AuthorCommandMapper
) {

    @get:ResponseStatus(HttpStatus.OK)
    @get:GetMapping(MappingConstants.AUTHORS_ONLY)
    val allAuthors
        get() = authorService.findAll()
                .map(authorResponseMapper::map)

    @get:ResponseStatus(HttpStatus.OK)
    @get:GetMapping(MappingConstants.AUTHORS)
    val allAuthorsWithBooks
        get() = authorService.findAllAuthorsWithBooks()

    @GetMapping(MappingConstants.AUTHORS + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getAuthor(@PathVariable id: String): AuthorResponse? {
        return authorService.getAuthorById(id)
                .let(authorResponseMapper::map)
    }

    @PostMapping(MappingConstants.AUTHORS)
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(@RequestBody authorRequest: AuthorRequest): AuthorResponse? {
        return authorService.createAuthor(authorRequest)
                .let(authorResponseMapper::map)
    }

    @PutMapping(MappingConstants.AUTHORS + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateAuthor(@PathVariable id: String, @RequestBody authorRequest: AuthorRequest): AuthorResponse? {
        return authorCommandMapper.map(id = id, request = authorRequest)
                .run(authorService::updateAuthor)
                .let(authorResponseMapper::map)
    }

    @DeleteMapping(MappingConstants.AUTHORS + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteAuthor(@PathVariable id: String) {
        return authorService.deleteAuthorById(id)
    }

}
