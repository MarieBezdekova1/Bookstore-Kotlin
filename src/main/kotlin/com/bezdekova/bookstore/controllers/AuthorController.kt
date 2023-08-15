package com.bezdekova.bookstore.controllers

import com.bezdekova.bookstore.constant.MappingConstants
import com.bezdekova.bookstore.mappers.response.AuthorResponseMapper
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.model.response.AuthorResponse
import com.bezdekova.bookstore.repositories.AuthorRepository
import com.bezdekova.bookstore.services.api.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
class AuthorController internal constructor(
        private val authorService: AuthorService,
        private val authorResponseMapper: AuthorResponseMapper
) {

    @get:ResponseStatus(HttpStatus.OK)
    @get:GetMapping(MappingConstants.AUTHORS)
    val allAuthors: List<AuthorResponse?>
        get() = authorService.allAuthors
                .map(authorResponseMapper::map)
                .toList()

    @GetMapping(MappingConstants.AUTHORS + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getAuthor(@PathVariable id: String): AuthorResponse? {
        return authorResponseMapper.map(authorService.getAuthorById(id))
    }

    @PostMapping(MappingConstants.AUTHORS)
    @ResponseStatus(HttpStatus.CREATED)
    fun createAuthor(@RequestBody authorRequest: AuthorRequest): AuthorResponse? {
        return authorResponseMapper.map(authorService.createAuthor(authorRequest))
    }

}
