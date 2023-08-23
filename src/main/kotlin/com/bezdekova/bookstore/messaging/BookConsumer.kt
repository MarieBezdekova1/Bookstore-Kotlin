package com.bezdekova.bookstore.messaging

import com.bezdekova.bookstore.constant.QueueConstants
import com.bezdekova.bookstore.mappers.domain.BookDomainMapper
import com.bezdekova.bookstore.model.request.BookRequest
import com.bezdekova.bookstore.repositories.BookRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class BookListener(
        private val bookRepository: BookRepository,
        private val bookDomainMapper: BookDomainMapper
) {

    val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @RabbitListener(queues = [QueueConstants.BOOK_QUEUE])
    fun onBookRegistration(bookRequest: BookRequest) {
        log.info("Received [$bookRequest]")
        bookDomainMapper.map(bookRequest).run(bookRepository::insert)
    }
}