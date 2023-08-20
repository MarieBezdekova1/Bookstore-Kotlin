package com.bezdekova.bookstore.services.listener

import com.bezdekova.bookstore.mappers.domain.BookDomainMapper
import com.bezdekova.bookstore.model.request.BookRequest
import com.bezdekova.bookstore.repositories.BookRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class BookListener(
        private val bookRepository: BookRepository,
        private val bookDomainMapper: BookDomainMapper
) {

    fun onBookRegistration(message: String) {
        //println("Received [$message]")

        // save book
        val bookRequest = ObjectMapper().readValue(message, BookRequest::class.java)
        bookDomainMapper.map(bookRequest).run(bookRepository::insert)
    }
}