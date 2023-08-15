package com.bezdekova.bookstore

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.repositories.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootApplication
class BookstoreApplication : CommandLineRunner {

    @Autowired
    private lateinit var repository: AuthorRepository

    @Autowired
    private lateinit var template: MongoTemplate

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<BookstoreApplication>(*args)
        }
    }

    override fun run(vararg args: String?) {
        repository.deleteAll()

        template.save(Author("052345234", "Bob Smith"))
        template.save(Author("045345", "Alice Smith"))

    }
}
