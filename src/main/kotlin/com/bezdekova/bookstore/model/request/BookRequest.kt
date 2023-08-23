package com.bezdekova.bookstore.model.request

import java.io.Serializable

@JvmRecord
data class BookRequest(
        val name: String,
        val price: Int?,
        val authorId: String?
) : Serializable
