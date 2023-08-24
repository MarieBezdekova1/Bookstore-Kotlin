package com.bezdekova.bookstore.services

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.mappers.csv.AuthorCsvMapper
import com.bezdekova.bookstore.mappers.domain.AuthorDomainMapper
import com.bezdekova.bookstore.mappers.response.BookShortResponseMapper
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.model.response.AuthorWithBooksResponse
import com.bezdekova.bookstore.properties.CsvProperties
import com.bezdekova.bookstore.repositories.AuthorRepository
import com.bezdekova.bookstore.repositories.BookRepository
import com.bezdekova.bookstore.services.api.AuthorService
import com.bezdekova.bookstore.utils.getNow
import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class AuthorServiceImpl(
        private val authorRepository: AuthorRepository,
        private val bookRepository: BookRepository,
        private val authorDomainMapper: AuthorDomainMapper,
        private val bookShortResponseMapper: BookShortResponseMapper,
        private val authorCsvMapper: AuthorCsvMapper,
        private val csvProperties: CsvProperties
) : AuthorService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun findAll(): Page<Author> {
        return Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")
        ).let {
            PageRequest.of(0, 5, it)
        }.run(authorRepository::findAll)
    }

    override fun findAllAuthorsWithBooks(): List<AuthorWithBooksResponse> {
        val authors = authorRepository.findAll()
        return authors.map { author ->
            val books = bookRepository.findBookByAuthor(author).map(bookShortResponseMapper::map)
            AuthorWithBooksResponse(author.name, books)
        }
    }

    override fun getAuthorById(id: String): Author {
        return authorRepository.findById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id $id not found.")
    }

    override fun createAuthor(authorRequest: AuthorRequest): Author {
        return authorDomainMapper.map(authorRequest).run(authorRepository::insert)
    }

    override fun updateAuthor(command: AuthorUpdateCommand): Author {
        return authorRepository.update(command)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id ${command.id} not found or something went bad during update.")
    }

    override fun deleteAuthorById(id: String) {
        authorRepository.findById(id)?.run {
            authorRepository.deleteById(id)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id $id not found.")
    }

    override fun importAuthorsFromCsv(file: MultipartFile) {
        val authorList = mutableListOf<Author>()
        val reader = CSVReaderBuilder(InputStreamReader(file.inputStream)).build()

        val batchSize = csvProperties.batchSize
        var count = 0
        var maxCount = 0

        reader.use { csvReader ->
            var nextLine: Array<String>?
            while (csvReader.readNext().also { nextLine = it } != null) {
                nextLine?.let(authorCsvMapper::map)?.also(authorList::add)

                if (++count >= batchSize) authorList.let {
                    authorRepository.insertAll(it)
                    it.clear()
                    maxCount += count
                    log.info("Inserted $maxCount")
                    count = 0
                }
            }
        }

        if (authorList.isNotEmpty()) {
            authorRepository.insertAll(authorList)
            maxCount += count
            log.info("Inserted $maxCount authors.")
        }

    }

    fun exportAuthorsToCsvSimple(filePath: String) {
        val authors = authorRepository.findAll()

        CSVWriter(FileWriter(filePath)).use { csvWriter ->
            csvWriter.writeNext(arrayOf("ID", "Name"))
            authors.forEach { author ->
                csvWriter.writeNext(arrayOf(author.id.toString(), author.name))
            }
        }
    }

    override fun exportAuthorsToCsv(): ResponseEntity<StreamingResponseBody> {
        val fileName = "${csvProperties.exportFilePath}/authorsExport-${getNow()}.csv"
        val batchSize = csvProperties.batchSize

        CSVWriter(FileOutputStream(File(fileName)).writer()).use { csvWriter ->
            csvWriter.writeNext(arrayOf("ID", "Name"))

            var page = 0
            var authors: List<Author>

            do {
                authors = Sort.by(
                        Sort.Order.asc("name"),
                        Sort.Order.desc("id")
                ).let { PageRequest.of(page++, batchSize, it) }
                        .let { pageable ->
                            authorRepository.findAll(pageable).content
                        }
                authors.forEach { author ->
                    csvWriter.writeNext(arrayOf(author.id.toString(), author.name))
                }
                log.info("Exporting page $page for batch size $batchSize")
            } while (authors.isNotEmpty())
        }

        val responseBody = StreamingResponseBody { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.write("Export done into $fileName")
            }
        }

        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        return ResponseEntity.ok()
                .headers(headers)
                .body(responseBody)
    }

}
