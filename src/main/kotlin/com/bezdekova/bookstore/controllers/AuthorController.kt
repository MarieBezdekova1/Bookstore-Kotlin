package com.bezdekova.bookstore.controllers

import com.bezdekova.bookstore.constant.MappingConstants.AUTHORS
import com.bezdekova.bookstore.constant.MappingConstants.AUTHORS_ID
import com.bezdekova.bookstore.constant.MappingConstants.AUTHORS_ONLY
import com.bezdekova.bookstore.constant.MappingConstants.EXPORT_AUTHORS
import com.bezdekova.bookstore.constant.MappingConstants.IMPORT_AUTHORS
import com.bezdekova.bookstore.constant.MappingConstants.IMPORT_AUTHORS_DEFAULT
import com.bezdekova.bookstore.mappers.command.AuthorCommandMapper
import com.bezdekova.bookstore.mappers.response.AuthorResponseMapper
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.model.response.AuthorResponse
import com.bezdekova.bookstore.model.response.AuthorWithBooksResponse
import com.bezdekova.bookstore.properties.CsvProperties
import com.bezdekova.bookstore.services.api.AuthorService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.File
import java.nio.file.Files

@RestController
@SecurityRequirement(name = "securityschema")
class AuthorController internal constructor(
        private val authorService: AuthorService,
        private val authorResponseMapper: AuthorResponseMapper,
        private val authorCommandMapper: AuthorCommandMapper,
        private val csvProperties: CsvProperties
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
    fun importAuthors(@RequestParam file: MultipartFile) {
        authorService.importAuthorsFromCsv(file)
    }

    @PostMapping(IMPORT_AUTHORS_DEFAULT)
    @ResponseStatus(HttpStatus.OK)
    fun importAuthorsDefault() {
        val filePath = csvProperties.authorsFilePath
        val file = File(filePath)
        val multipartFile = MockMultipartFile(
                file.name, file.name, "application/octet-stream", Files.readAllBytes(file.toPath()))
        authorService.importAuthorsFromCsv(multipartFile)
    }

    @GetMapping(EXPORT_AUTHORS)
    @ResponseStatus(HttpStatus.OK)
    fun exportAuthors(): StreamingResponseBody {
        return authorService.exportAuthorsToCsv()
    }

}
