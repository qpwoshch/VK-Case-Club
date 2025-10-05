package com.fiveBoys.rustore

import com.google.gson.Gson

class Parser {
    private val gson = Gson()
    fun parse(json: String): App {
        return gson.fromJson(json, App::class.java)
    }
}
