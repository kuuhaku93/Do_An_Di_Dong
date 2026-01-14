package com.example.libraryoffreelancer.viewmodel

import android.util.Log
import com.example.libraryoffreelancer.model.Check
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

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