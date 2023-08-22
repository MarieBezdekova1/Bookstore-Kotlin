package com.bezdekova.bookstore.services

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.mappers.domain.BookDomainMapper
import com.bezdekova.bookstore.model.command.BookUpdateCommand
import com.bezdekova.bookstore.model.request.BookRequest
import com.bezdekova.bookstore.repositories.BookRepository
import com.bezdekova.bookstore.services.api.BookService
import com.fasterxml.jackson.databind.ObjectMapper
import com.opencsv.CSVReaderBuilder
import mu.KotlinLogging.logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.io.FileReader


@Service
class BookServiceImpl(
        private val bookRepository: BookRepository,
        private val bookDomainMapper: BookDomainMapper,
        private val rabbitTemplate: RabbitTemplate
) : BookService {

    val log = logger {}
    override fun findAll(): Page<Book> {
        val pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")))
        return bookRepository.findAll(pageable)
    }

    override fun getBookById(id: String): Book {
        return bookRepository.findById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id $id not found.")
    }

    override fun createBook(bookRequest: BookRequest): Book {
        return bookDomainMapper.map(bookRequest).run(bookRepository::insert)
    }

    override fun updateBook(command: BookUpdateCommand): Book {
        return bookRepository.update(command)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id ${command.id} not found or something went bad during update.")
    }

    override fun deleteBookById(id: String) {
        bookRepository.findById(id)?.run {
            bookRepository.deleteById(id)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id $id not found.")
    }

    override fun addNewBook(bookRequest: BookRequest): String {
        val bookAsString = ObjectMapper().writeValueAsString(bookRequest)

        // mělo by být možné posílat i objekty, není potřeba jen string.
        // Zároveň nikde v kodu nevidim consumera
        // Taky je good practise dávat věci týkající se queue do nový komponent/servis
        //rabbitTemplate.convertAndSend("spring-boot-exchange-direct","book", bookAsString)
        rabbitTemplate.convertAndSend("spring-boot-exchange-direct","book", bookRequest)
        return bookAsString
    }

    override fun importBooksFromCsv(filePath: String) {
        val bookList = mutableListOf<Book>()
        val reader = CSVReaderBuilder(FileReader(filePath)).build()

        // toto by mohlo být configurovatelné z nastavení - jinak ale dobrý přístup, jinak by to mohlo padat na heap space
        val batchSize = 10000
        var count = 0
        var maxCount = 0

        reader.use { csvReader ->
            var nextLine: Array<String>?
            while (csvReader.readNext().also { nextLine = it } != null) {
                // toto se dá napsat lepším zápisem pomocí "?" - dělá to za tebe null check a pokud je to null, tak se to neprovede
                /*
                val name = nextLine?.get(0)
                val price = nextLine?.get(1)?.toInt()
                val authorId = nextLine?.get(2)
                if (!name.isNullOrBlank()) {
                    BookRequest(name, price, authorId)
                            .let { bookDomainMapper.map(it) }
                            .run { bookList.add(this) }
                }*/
                nextLine?.let {
                    // toto by se správně mělo přesunout do mapperu
                    BookRequest(
                        // v kotlinu je běžná praxe používat named params, tzn. jmeno_parametru = hodnota
                        name = it[0],
                        // kotlin má navíc nějaké extension funkce. Například toIntOrNull je fajn, když nechceš vyhazovat chybu, pokud to nejde převést
                        price = it[1].toIntOrNull(),
                        authorId = it[2]
                    )
                }?.let(bookDomainMapper::map)
                    ?.run(bookList::add)

                if (++count >= batchSize) bookList.let {
                    bookRepository.insertAll(it)
                    it.clear()
                    maxCount += count
                    // používej log
                    println("Inserted $maxCount")
                    count = 0
                }
            }
        }

        if (bookList.isNotEmpty()) {
            bookRepository.insertAll(bookList)
        }

    }

}
