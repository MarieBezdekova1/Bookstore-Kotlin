package com.bezdekova.bookstore.config

import com.bezdekova.bookstore.constant.QueueConstants.BOOK_QUEUE
import com.bezdekova.bookstore.constant.QueueConstants.TEST_QUEUE
import com.bezdekova.bookstore.services.listener.BookListener
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange("spring-boot-exchange-direct")
    }

    @Bean(name= ["bookQueue"])
    fun createQueue(): Queue {
        return Queue(BOOK_QUEUE)
    }

    @Bean(name= ["testQueue"])
    fun createQueue2(): Queue {
        return Queue(TEST_QUEUE)
    }

    @Bean
    fun binding(@Qualifier("bookQueue") queue: Queue, exchange: DirectExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with("book")
    }

    @Bean
    fun binding2(@Qualifier("testQueue") queue2: Queue, exchange: DirectExchange): Binding {
        return BindingBuilder.bind(queue2).to(exchange).with("test")
    }

    @Bean
    fun listenerAdapter(bookListener: BookListener): MessageListenerAdapter {
        return MessageListenerAdapter(bookListener, "onBookRegistration")
    }

    @Bean
    fun container(connectionFactory: ConnectionFactory, listenerAdapter: MessageListenerAdapter): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer(connectionFactory)
        container.setQueueNames(BOOK_QUEUE)
        container.setMessageListener(listenerAdapter)
        return container
    }

}
