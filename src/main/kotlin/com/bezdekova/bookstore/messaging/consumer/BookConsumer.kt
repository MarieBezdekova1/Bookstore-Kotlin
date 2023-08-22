package com.bezdekova.bookstore.messaging.consumer

import com.bezdekova.bookstore.constant.QueueConstants.BOOK_QUEUE
import com.bezdekova.bookstore.model.request.BookRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class BookConsumer(
  private val objectMapper: ObjectMapper
) {

  @RabbitListener(queues = [BOOK_QUEUE])
  fun bookQueueConsumer(message: String) {
    val request = objectMapper.convertValue<BookRequest>(message)
  }
}
