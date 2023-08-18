package com.bezdekova.bookstore.repositories

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AuthorRepository {
    fun findById(id: String): Author?
    fun findAll(page: Pageable): Page<Author>
    fun findAll(): List<Author>
    fun insert(domain: Author): Author
    fun update(command: AuthorUpdateCommand): Author?
    fun deleteById(id: String): DeleteResult
}

