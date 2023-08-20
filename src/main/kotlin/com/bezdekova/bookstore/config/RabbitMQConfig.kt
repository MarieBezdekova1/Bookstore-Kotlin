package com.bezdekova.bookstore.config

import com.bezdekova.bookstore.services.listener.BookListener
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitMQConfig {

    /*
    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange("spring-boot-exchange")
    }
    */

    @Bean
    fun createQueue(): Queue {
        return Queue("book-registration")
    }

    /*
    @Bean
    fun binding(queue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with("")
    }
     */

    @Bean
    fun listenerAdapter(bookListener: BookListener): MessageListenerAdapter {
        return MessageListenerAdapter(bookListener, "onBookRegistration")
    }

    @Bean
    fun container(connectionFactory: ConnectionFactory, listenerAdapter: MessageListenerAdapter): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer(connectionFactory)
        container.setQueueNames("book-registration")
        container.setMessageListener(listenerAdapter)
        return container
    }

}