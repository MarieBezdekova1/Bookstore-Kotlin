package com.bezdekova.bookstore.model.request

import org.bson.types.ObjectId

@JvmRecord
data class AuthorRequest(val id: ObjectId, val name: String)
