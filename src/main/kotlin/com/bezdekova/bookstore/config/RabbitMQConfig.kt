package com.bezdekova.bookstore.config

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

    private val QUEUE_NAME = "book-registration"
    private val QUEUE_NAME2 = "test-queue"

    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange("spring-boot-exchange-direct")
    }

    @Bean(name= ["bookQueue"])
    fun createQueue(): Queue {
        return Queue(QUEUE_NAME)
    }

    @Bean(name= ["testQueue"])
    fun createQueue2(): Queue {
        return Queue(QUEUE_NAME2)
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
        container.setQueueNames(QUEUE_NAME)
        container.setMessageListener(listenerAdapter)
        return container
    }

}