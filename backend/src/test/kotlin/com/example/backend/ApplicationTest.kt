package com.example.backend

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {

    @Test
    fun testHealthEndpoint() = testApplication {
        application {
            module()
        }
        client.get("/health").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("OK", bodyAsText())
        }
    }

    @Test
    fun testAppsStartEndpoint() = testApplication {
        application {
            module()
        }
        client.get("/apps/start").apply {
            assertEquals(HttpStatusCode.OK, status)
            val json = bodyAsText()
            assertTrue(json.contains("\"apps\""))
            assertTrue(json.contains("\"id\""))
            assertTrue(json.contains("\"name\""))
        }
    }

    @Test
    fun testCategoriesEndpoint() = testApplication {
        application {
            module()
        }
        client.get("/categories").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = bodyAsText()
            assertTrue(response.contains("\"name\""))
            assertTrue(response.contains("\"count\""))
        }
    }

    @Test
    fun testGetAppById() = testApplication {
        application {
            module()
        }
        client.get("/apps/1").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = bodyAsText()
            assertTrue(response.contains("\"Palette\""))
            assertTrue(response.contains("\"Инструменты\""))
        }
    }

    @Test
    fun testGetNonExistentApp() = testApplication {
        application {
            module()
        }
        client.get("/apps/999").apply {
            assertEquals(HttpStatusCode.NotFound, status)
            val response = bodyAsText()
            assertTrue(response.contains("not_found"))
        }
    }

    @Test
    fun testAppsPagination() = testApplication {
        application {
            module()
        }
        client.get("/apps?limit=2&offset=0").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = bodyAsText()
            assertTrue(response.contains("\"total\""))
            assertTrue(response.contains("\"limit\":2"))
        }
    }

    @Test
    fun testAppsFilterByCategory() = testApplication {
        application {
            module()
        }
        client.get("/apps?category=Инструменты").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = bodyAsText()
            assertTrue(response.contains("\"category\":\"Инструменты\""))
        }
    }

    @Test
    fun testAppsSearch() = testApplication {
        application {
            module()
        }
        client.get("/apps?q=Wildberries").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = bodyAsText()
            assertTrue(response.contains("\"Wildberries\""))
        }
    }
}
