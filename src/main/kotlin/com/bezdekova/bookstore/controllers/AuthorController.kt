package com.bezdekova.bookstore.controllers

import com.bezdekova.bookstore.constant.MappingConstants
import com.bezdekova.bookstore.mappers.response.AuthorResponseMapper
import com.bezdekova.bookstore.model.response.AuthorResponse
import com.bezdekova.bookstore.services.api.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorController internal constructor(authorService: AuthorService, authorResponseMapper: AuthorResponseMapper) {
    private val authorService: AuthorService
    private val authorResponseMapper: AuthorResponseMapper

    init {
        this.authorService = authorService
        this.authorResponseMapper = authorResponseMapper
    }

    @get:ResponseStatus(HttpStatus.OK)
    @get:GetMapping(MappingConstants.AUTHORS)
    val allAuthors: List<AuthorResponse?>
        get() = authorService.allAuthors
                .map(authorResponseMapper::map)
                .toList()

    @GetMapping(MappingConstants.AUTHORS + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getAuthor(@PathVariable id: Int): AuthorResponse? {
        return authorResponseMapper.map(authorService.getAuthorById(id))
    }
}
