package com.bezdekova.bookstore.repositories

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class CustomUserRepository(private val mongoTemplate: MongoTemplate) {

}