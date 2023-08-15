package com.bezdekova.bookstore.services

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.repositories.AuthorRepository
import com.bezdekova.bookstore.services.api.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthorServiceImpl @Autowired constructor(private val authorRepository: AuthorRepository) : AuthorService {

    override val allAuthors: List<Author>
        get() = authorRepository.findAll()

    override fun getAuthorById(id: String): Author {
        return authorRepository.findById(id)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.") }
    }

    override fun createAuthor(authorRequest: AuthorRequest): Author? {
        val author = Author(authorRequest.id, authorRequest.name)
        return authorRepository.save(author)
    }

    override fun updateAuthor(id: String, authorRequest: AuthorRequest): Author? {
        val author = authorRepository.findById(id)
        return if (author.isPresent) {
            val authorExists = author.get()
            authorExists.name = authorRequest.name
            authorRepository.save<Author>(authorExists)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.")
        }
    }

    override fun deleteAuthor(id: String) {
        val author = authorRepository.findById(id)
        return if (author.isPresent) {
            authorRepository.deleteById(id)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.")
        }
    }

}