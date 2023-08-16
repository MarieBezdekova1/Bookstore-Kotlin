package com.bezdekova.bookstore.repositories

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

// tady doporučuji používat Mongo template, ne MongoRepository. Umožňuje to lepší kontrolu nad tím, co chceš a jak to chceš získat (existují taky projekce). Zde teda jen interface.
// Zároveň u nás v projektech se to tak používá.
interface AuthorRepository {

  // například tady si můžeš říct, že ti to bude vracet nullable Author
  fun findById(id: String): Author?

  fun findAll(page: Pageable): Page<Author>
  fun insert(domain: Author): Author
  fun update(command: AuthorUpdateCommand): Author?
}

