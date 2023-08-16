package com.bezdekova.bookstore.services

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.bezdekova.bookstore.model.request.AuthorRequest
import com.bezdekova.bookstore.repositories.AuthorRepository
import com.bezdekova.bookstore.services.api.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
// nemusíš psát @Autowired constructor, stačí servisy a componenty jen přidat do builderu servisy
// servisa by nem2la vracet nullable objekty
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
) : AuthorService {

    // toto je cache? Nikde ji nevidím použitou - a pokud to je cache, tak to tady není potřeba, stačí funkce findAll
    override val allAuthors: Page<Author>
        get() = authorRepository.findAll()

    override fun getAuthorById(id: String): Author {
        return authorRepository.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id $id not found.")
    }

    override fun createAuthor(authorRequest: AuthorRequest): Author {
        // toto přesunout opět do AuthorDomainMapper
        val author = Author(authorRequest.id, authorRequest.name)
        return authorRepository.insert(author)
    }

    override fun updateAuthor(command: AuthorUpdateCommand): Author {
        // Tohle není potřeba dělat. Stačí použít update v mongo template.
//        val author = authorRepository.findById(id)
//        return if (author.isPresent) {
//            val authorExists = author.get()
//            authorExists.name = authorRequest.name
//            authorRepository.save<Author>(authorExists)
//        } else {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.")
//        }
        // lepší takto:
        return authorRepository.update(command)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author with id ${command.id} not found or something went bad during update.")
    }

    override fun deleteAuthor(id: String) {
        val author = authorRepository.findById(id)
        return if (author.isPresent) {
            authorRepository.deleteById(id)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found.")
        }
    }

}
