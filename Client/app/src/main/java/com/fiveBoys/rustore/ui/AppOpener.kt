package com.fiveBoys.rustore

class AppOpener {
    private val connection = Connect()
    private val parser = Parser()

    suspend fun open(id: String): App {
        val jsonData = connection.getData(id)
        return parser.parse(jsonData)
    }
}
