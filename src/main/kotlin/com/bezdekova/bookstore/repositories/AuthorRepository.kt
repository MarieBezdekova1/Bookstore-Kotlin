package com.bezdekova.bookstore.repositories

import com.bezdekova.bookstore.db.Author
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : MongoRepository<Author, String> {

}

