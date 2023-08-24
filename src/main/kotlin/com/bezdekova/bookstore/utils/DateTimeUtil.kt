package com.bezdekova.bookstore.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getNow(): String {
    return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").let(LocalDateTime.now()::format)
}
