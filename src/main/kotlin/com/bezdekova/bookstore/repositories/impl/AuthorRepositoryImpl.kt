package com.bezdekova.bookstore.repositories.impl

import com.bezdekova.bookstore.db.Author
import com.bezdekova.bookstore.model.command.AuthorUpdateCommand
import com.bezdekova.bookstore.repositories.AuthorRepository
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
class AuthorRepositoryImpl(
        private val mongoTemplate: MongoTemplate
) : AuthorRepository {
    override fun findById(id: String): Author? {
        return mongoTemplate.findOne(
                Query(Author::id isEqualTo id),
                Author::class.java
        )
    }

    override fun findAll(page: Pageable): Page<Author> {
        val count = mongoTemplate.count(Query(), Author::class.java)
        return Aggregation.newAggregation(
                Author::class.java,
                Aggregation.match(Criteria()),
                Aggregation.sort(page.sort),
                Aggregation.skip((page.pageNumber * page.pageSize).toLong()),
                Aggregation.limit(page.pageSize.toLong()),
        )
                .run { mongoTemplate.aggregate(this, Author::class.java) }
                .let { PageableExecutionUtils.getPage(it.mappedResults, page) { count } }
    }

    override fun findAll(): List<Author> {
        return mongoTemplate.findAll(Author::class.java)
    }

    override fun insert(domain: Author): Author {
        return mongoTemplate.insert(domain)
    }

    override fun update(command: AuthorUpdateCommand): Author? = with(command) {
        val query = Query(Author::id isEqualTo id)
        val update = Update().apply {
            set(Author::name.toDotPath(), name)
        }
        mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                Author::class.java
        )
    }

    override fun deleteById(id: String): DeleteResult {
        return mongoTemplate.remove(
                Query(Author::id isEqualTo id),
                Author::class.java
        )
    }
}
