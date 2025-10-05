package com.example.backend

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.*
import java.io.File

class UtilsTest {

    @Test
    fun testSha256Calculation() = testApplication {
        val tempFile = File.createTempFile("test", ".txt")
        tempFile.writeText("test content")

        try {
            val hash = sha256(tempFile)
            assertNotNull(hash)
            assertEquals(64, hash.length) // SHA-256 hash всегда 64 символа
            assertTrue(hash.matches(Regex("[0-9a-f]+")))
        } finally {
            tempFile.delete()
        }
    }

    @Test
    fun testAppToJsonWithFields() = testApplication {
        val testApp = AppDto(
            id = "test",
            name = "Test App",
            developer = "Test Dev",
            category = "Test",
            ratingAge = "6+",
            shortDesc = "Short",
            fullDesc = "Full",
            iconUrl = "http://test/icon.png",
            screenshots = emptyList(),
            apkUrl = "http://test/app.apk",
            apkSize = 1024L,
            apkSha256 = "abc123"
        )

        val fields = setOf("id", "name", "category")
        val json = appToJson(testApp, fields)

        assertEquals(3, json.keys.size)
        assertTrue("id" in json)
        assertTrue("name" in json)
        assertTrue("category" in json)
        assertFalse("developer" in json) // Не должно быть включено
    }

    @Test
    fun testAppsToJsonArray() = testApplication {
        val apps = listOf(
            AppDto(
                id = "1", name = "App1", developer = "Dev1", category = "Cat1",
                ratingAge = "6+", shortDesc = "Desc1", fullDesc = "Full1",
                iconUrl = "icon1", screenshots = emptyList(), apkUrl = "apk1"
            ),
            AppDto(
                id = "2", name = "App2", developer = "Dev2", category = "Cat2",
                ratingAge = "12+", shortDesc = "Desc2", fullDesc = "Full2",
                iconUrl = "icon2", screenshots = emptyList(), apkUrl = "apk2"
            )
        )

        val fields = setOf("id", "name")
        val jsonArray = appsToJsonArray(apps, fields)

        assertEquals(2, jsonArray.size)
        val firstApp = jsonArray[0] as? kotlinx.serialization.json.JsonObject
        assertNotNull(firstApp)
        assertEquals(2, firstApp.keys.size)
        assertTrue("id" in firstApp)
        assertTrue("name" in firstApp)
    }
}
