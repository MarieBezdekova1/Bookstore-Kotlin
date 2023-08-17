package com.bezdekova.bookstore

import com.bezdekova.bookstore.constant.MappingConstants
import org.json.JSONException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun testGetAllAuthors() {
        val response: ResponseEntity<String> = restTemplate.getForEntity(
                "http://localhost:$port/${MappingConstants.AUTHORS}",
                String::class)

        assertEquals(HttpStatus.OK, response.statusCode, "Response should be 200.")
        assertEquals(MediaType.APPLICATION_JSON, response.headers.getContentType(), "Type should be application/json.")

        val responseBody = response.body
        assertNotNull(responseBody, "Response should not be null.")
    }

    @Test
    @Throws(JSONException::class)
    fun testRetrieveAuthorById() {
        val response: ResponseEntity<String> = restTemplate.getForEntity(
                "http://localhost:$port/${MappingConstants.AUTHORS}/1",
                String::class)

        assertEquals(HttpStatus.OK, response.statusCode, "Response should be 200.")
        assertEquals(MediaType.APPLICATION_JSON, response.headers.getContentType(), "Type should be application/json.")

        val responseBody = response.body
        assertNotNull(responseBody, "Response should not be null.")
    }

}
