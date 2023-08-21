package com.bezdekova.bookstore.repositories

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.model.command.BookUpdateCommand
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BookRepository {

    fun findById(id: String): Book?
    fun findBookByAuthor(author: Author): List<Book>
    fun findAll(page: Pageable): Page<Book>
    fun insert(domain: Book): Book
    fun insertAll(books: List<Book>): MutableCollection<Book>
    fun update(command: BookUpdateCommand): Book?
    fun deleteById(id: String): DeleteResult

}
