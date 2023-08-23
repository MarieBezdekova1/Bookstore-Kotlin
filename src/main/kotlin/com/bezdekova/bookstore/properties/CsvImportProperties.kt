package com.bezdekova.bookstore.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "csv-import")
class CSVImportProperties(
        val authorsFilePath: String,
        val booksFilePath: String,
        val batchSize: Int
)