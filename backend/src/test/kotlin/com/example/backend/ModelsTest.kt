package com.example.backend

import kotlin.test.*
import kotlinx.serialization.json.*

class ModelsTest {

    @Test
    fun testAppDtoSerialization() {
        val app = AppDto(
            id = "1",
            name = "Test App",
            developer = "Test Developer",
            category = "Инструменты",
            ratingAge = "6+",
            shortDesc = "Short description",
            fullDesc = "Full description",
            iconUrl = "http://test/icon.png",
            screenshots = listOf("http://test/screen1.png", "http://test/screen2.png"),
            apkUrl = "http://test/app.apk",
            apkSize = 1024L,
            apkSha256 = "abc123"
        )

        val json = Json.encodeToString(AppDto.serializer(), app)
        val parsed = Json.decodeFromString(AppDto.serializer(), json)

        assertEquals(app.id, parsed.id)
        assertEquals(app.name, parsed.name)
        assertEquals(app.category, parsed.category)
        assertEquals(app.screenshots.size, parsed.screenshots.size)
    }

    @Test
    fun testErrorDto() {
        val error = error("test_error", "Test message")
        assertEquals("test_error", error.error)
        assertEquals("Test message", error.message)
    }

    @Test
    fun testPagedAppsDto() {
        val apps = listOf(
            AppDto(
                id = "1", name = "App1", developer = "Dev1", category = "Cat1",
                ratingAge = "6+", shortDesc = "Desc1", fullDesc = "Full1",
                iconUrl = "icon1", screenshots = emptyList(), apkUrl = "apk1"
            )
        )

        val paged = PagedAppsDto(items = apps, total = 100, limit = 50, offset = 0)

        assertEquals(1, paged.items.size)
        assertEquals(100, paged.total)
        assertEquals(50, paged.limit)
        assertEquals(0, paged.offset)
    }
}