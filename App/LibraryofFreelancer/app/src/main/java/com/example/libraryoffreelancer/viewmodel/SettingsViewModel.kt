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

    fun Logout(token: String?): Check{
        var check = Check(false, "")
        val res= Request.Builder()
            .url("$urlRoot/logout/")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .get()
            .build()

        val thread = Thread{
            client.newCall(res).execute().use { response ->
                val body=response.body?.string().orEmpty()
                Log.d("md",body)
                if (response.isSuccessful){
                    check.isSuccess=true
                    check.message = response.message
                }
                else{
                    check.isSuccess = false
                    check.message = response.message
                }
            }
        }
        thread.start()
        thread.join()
        return check
    }
}

fun dateconvert(input: String): String {

    val instant = Instant.parse(input)
    val zoneId = ZoneId.of("Asia/Ho_Chi_Minh")
    val zonedDateTime = instant.atZone(zoneId)
    val formatter = DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy")
    val output = zonedDateTime.format(formatter)
    return output
}
