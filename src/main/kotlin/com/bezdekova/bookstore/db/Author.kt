package com.bezdekova.bookstore.db

import jakarta.persistence.*

@Entity
@Table(name = "authors")
class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
    lateinit var name: String

    protected constructor()
    constructor(name: String) {
        this.name = name
    }

}
