package com.bezdekova.bookstore

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.repositories.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookstoreApplication : CommandLineRunner {

    @Autowired
    private lateinit var repository: AuthorRepository

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<BookstoreApplication>(*args)
        }
    }

    override fun run(vararg args: String?) {
        repository.deleteAll()

        // Save a couple of customers
        repository.save(Author("1", "Alice Smith"))
        repository.save(Author("2", "Bob Smith"))

        println(repository.findByName("Alice Smith"))

        println("Authors found with findAll():")
        println("-------------------------------")
        for (author in repository.findAll()) {
            println(author)
        }
        println()
        /*

        println("Customer found with findByName('Alice Smith'):")
        println("--------------------------------")
        println(repository.findByName("Alice Smith"))

        println("Customers found with findByLastName('Smith'):")
        println("--------------------------------")
        for (author in repository.findAllByName("Alice Smith")!!) {
            println(author)
        }
         */

    }
}
