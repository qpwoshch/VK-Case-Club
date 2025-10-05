package com.fiveBoys.rustore

import kotlinx.coroutines.runBlocking

class AppOpener {
    private val connection = Connect()
    private val parser = Parser()

    fun open(id: String): App {
        var app: App? = null

        runBlocking {
            val jsonData = connection.getData(id)
            app = parser.parse(jsonData)
        }

        return app ?: throw Exception("App not found")
    }
}