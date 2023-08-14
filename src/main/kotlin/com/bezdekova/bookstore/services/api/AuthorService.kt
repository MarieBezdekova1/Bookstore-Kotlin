package com.bezdekova.bookstore.services.api

import com.bezdekova.bookstore.db.Author

interface AuthorService {
    val allAuthors: List<Author>
    fun getAuthorById(id: Int): Author?
}
