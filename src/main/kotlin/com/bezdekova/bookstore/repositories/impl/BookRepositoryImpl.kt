package com.bezdekova.bookstore.repositories.impl

import com.bezdekova.bookstore.db.Book
import com.bezdekova.bookstore.model.command.BookUpdateCommand
import com.bezdekova.bookstore.repositories.AuthorRepository
import com.bezdekova.bookstore.repositories.BookRepository
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class BookRepositoryImpl(
        private val mongoTemplate: MongoTemplate,
        private val authorRepository: AuthorRepository
) : BookRepository {
    override fun findById(id: String): Book? {
        return mongoTemplate.findOne(
                Query(Book::id isEqualTo id),
                Book::class.java
        )
    }

    override fun findAll(page: Pageable): Page<Book> {
        val count = mongoTemplate.count(Query(), Book::class.java)
        return Aggregation.newAggregation(
                Book::class.java,
                Aggregation.match(Criteria()),
                Aggregation.sort(page.sort),
                Aggregation.skip((page.pageNumber * page.pageSize).toLong()),
                Aggregation.limit(page.pageSize.toLong()),
        )
                .run { mongoTemplate.aggregate(this, Book::class.java) }
                .let { PageableExecutionUtils.getPage(it.mappedResults, page) { count } }
    }

    override fun insert(domain: Book): Book {
        return mongoTemplate.insert(domain)
    }

    override fun update(command: BookUpdateCommand): Book? = with(command) {
        val query = Query(Book::id isEqualTo id)
        val author = authorRepository.findById(authorId)
        val update = Update().apply {
            set(Book::name.toDotPath(), name)
            set(Book::price.toDotPath(), price)
            set(Book::author.toDotPath(), author)
        }
        mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                Book::class.java
        )
    }

    override fun deleteById(id: String): DeleteResult {
        return mongoTemplate.remove(
                Query(Book::id isEqualTo id),
                Book::class.java
        )
    }
}
