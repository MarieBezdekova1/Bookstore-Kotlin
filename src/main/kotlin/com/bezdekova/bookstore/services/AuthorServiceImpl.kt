package com.bezdekova.bookstore.services

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.mappers.domain.AuthorDomainMapper
import com.bezdekova.bookstore.mappers.response.BookShortResponseMapper
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.model.response.AuthorWithBooksResponse
import com.bezdekova.bookstore.repositories.AuthorRepository
import com.bezdekova.bookstore.repositories.BookRepository
import com.bezdekova.bookstore.services.api.AuthorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthorServiceImpl(
        private val authorRepository: AuthorRepository,
        private val bookRepository: BookRepository,
        private val authorDomainMapper: AuthorDomainMapper,
        private val bookShortResponseMapper: BookShortResponseMapper
) : AuthorService {
    override fun findAll(): Page<Author> {
        return Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")
        ).let {
            PageRequest.of(0, 5, it)
        }.run(authorRepository::findAll)
    }

    override fun findAllAuthorsWithBooks(): List<AuthorWithBooksResponse> {
        val authors = authorRepository.findAll()
        return authors.map { author ->
            val books = bookRepository.findBookByAuthor(author).map(bookShortResponseMapper::map)
            AuthorWithBooksResponse(author.name, books)
        }
    }

    override fun getAuthorById(id: String): Author {
        return authorRepository.findById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id $id not found.")
    }

    override fun createAuthor(authorRequest: AuthorRequest): Author {
        return authorDomainMapper.map(authorRequest).run(authorRepository::insert)
    }

    override fun updateAuthor(command: AuthorUpdateCommand): Author {
        return authorRepository.update(command)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id ${command.id} not found or something went bad during update.")
    }

    override fun deleteAuthorById(id: String) {
        authorRepository.findById(id)?.run {
            authorRepository.deleteById(id)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id $id not found.")
    }
}
