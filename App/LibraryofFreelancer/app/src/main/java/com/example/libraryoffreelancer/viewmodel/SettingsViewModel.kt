package com.example.libraryoffreelancer.viewmodel

import android.util.Log
import com.example.libraryoffreelancer.model.Check
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val json = Json{
    ignoreUnknownKeys = true
}
class SettingsViewModel {
    val client= OkHttpClient()
    val urlRoot="http://10.0.2.2:8000/api"


}

fun dateconvert(input: String): String {

    val instant = Instant.parse(input)
    val zoneId = ZoneId.of("Asia/Ho_Chi_Minh")
    val zonedDateTime = instant.atZone(zoneId)
    val formatter = DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy")
    val output = zonedDateTime.format(formatter)
    return output
}
