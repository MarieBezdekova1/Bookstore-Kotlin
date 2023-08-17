package com.bezdekova.bookstore

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.repositories.AuthorRepository
import org.bson.types.ObjectId
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookstoreApplication(private var repository: AuthorRepository, ) : CommandLineRunner {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<BookstoreApplication>(*args)
        }
    }

    override fun run(vararg args: String?) {
        repository.insert(Author(ObjectId(),"Bob Smith"))
        repository.insert(Author(ObjectId(), "Alice Smith"))

    }
}
