package com.bezdekova.bookstore.services

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.mappers.domain.BookDomainMapper
import com.bezdekova.bookstore.model.command.BookUpdateCommand
import com.bezdekova.bookstore.model.request.BookRequest
import com.bezdekova.bookstore.repositories.BookRepository
import com.bezdekova.bookstore.services.api.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class BookServiceImpl(
        private val bookRepository: BookRepository,
        private val bookDomainMapper: BookDomainMapper
) : BookService {
    override fun findAll(): Page<Book> {
        val pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")))
        return bookRepository.findAll(pageable)
    }

    override fun getBookById(id: String): Book {
        return bookRepository.findById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id $id not found.")
    }

    override fun createBook(bookRequest: BookRequest): Book {
        return bookDomainMapper.map(bookRequest).run(bookRepository::insert)
    }

    override fun updateBook(command: BookUpdateCommand): Book {
        return bookRepository.update(command)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id ${command.id} not found or something went bad during update.")
    }

    override fun deleteBookById(id: String) {
        bookRepository.findById(id)?.run {
            bookRepository.deleteById(id)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id $id not found.")
    }


}