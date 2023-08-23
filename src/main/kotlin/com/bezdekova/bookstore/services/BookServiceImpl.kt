package com.bezdekova.bookstore.services

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.mappers.csv.BookCsvMapper
import com.bezdekova.bookstore.mappers.domain.BookDomainMapper
import com.bezdekova.bookstore.model.command.BookUpdateCommand
import com.bezdekova.bookstore.model.request.BookRequest
import com.bezdekova.bookstore.properties.CSVImportProperties
import com.bezdekova.bookstore.repositories.BookRepository
import com.bezdekova.bookstore.services.api.BookService
import com.opencsv.CSVReaderBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.InputStreamReader

@Service
class BookServiceImpl(
        private val bookRepository: BookRepository,
        private val bookDomainMapper: BookDomainMapper,
        private val bookCsvMapper: BookCsvMapper,
        private val csvImportProperties: CSVImportProperties
) : BookService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

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

    override fun importBooksFromCsv(file: MultipartFile) {
        val bookList = mutableListOf<Book>()
        val reader = CSVReaderBuilder(InputStreamReader(file.inputStream)).build()

        val batchSize = csvImportProperties.batchSize
        var count = 0
        var maxCount = 0

        reader.use { csvReader ->
            var nextLine: Array<String>?
            while (csvReader.readNext().also { nextLine = it } != null) {
                nextLine?.let {
                    val book = bookCsvMapper.map(it)
                    bookList.add(book)
                }

                if (++count >= batchSize) bookList.let {
                    bookRepository.insertAll(it)
                    it.clear()
                    maxCount += count
                    log.info("Inserted $maxCount")
                    count = 0
                }

            }
        }

        if (bookList.isNotEmpty()) {
            bookRepository.insertAll(bookList)
            maxCount += count
            log.info("Inserted $maxCount books.")
        }

    }

}