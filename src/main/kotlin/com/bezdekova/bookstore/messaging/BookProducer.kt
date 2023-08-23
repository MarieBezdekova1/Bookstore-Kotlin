package com.bezdekova.bookstore.messaging

import com.bezdekova.bookstore.model.request.BookRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class BookProducer(private val rabbitTemplate: RabbitTemplate) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    fun addNewBook(bookRequest: BookRequest) {
        log.info("Registered a book $bookRequest")
        rabbitTemplate.convertAndSend("spring-boot-exchange-direct","book", bookRequest)
    }
}