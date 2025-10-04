package com.fiveBoys.rustore

class AppOpener {
    private val connection = Connect()
    private val parser = Parser()

    fun open(name : String) : App {
        val app : App = parser.parse(connection.getData(name))
        return app
    }
}