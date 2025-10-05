package com.fiveBoys.rustore

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Connect {

    private val client = OkHttpClient()

    suspend fun getData(id: String): String {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("http://192.168.0.115:8080/apps/$id")
                .build()
            val response: Response = client.newCall(request).execute()
            response.body?.string() ?: ""


        }
    }
}
