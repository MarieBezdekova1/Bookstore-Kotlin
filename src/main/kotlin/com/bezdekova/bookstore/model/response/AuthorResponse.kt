package com.bezdekova.bookstore.model.response

import org.bson.types.ObjectId

@JvmRecord
data class AuthorResponse(val id: ObjectId, val name: String)
