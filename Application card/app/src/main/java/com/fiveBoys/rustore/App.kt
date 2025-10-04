package com.fiveBoys.rustore
import java.io.Serializable

data class App (
    val id : Int,
    val name : String,
    val developer : String,
    val category : String,
    val ratingAge : String,
    val fullDesc : String,
    val iconUrl : String,
    val screenshots : List<String>,
    val apkUrl : String,
    val apkSize : Int
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
