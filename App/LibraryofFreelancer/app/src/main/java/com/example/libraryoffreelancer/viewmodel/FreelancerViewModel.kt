package com.example.libraryoffreelancer.viewmodel

import android.util.Log
import com.example.libraryoffreelancer.model.Account
import com.example.libraryoffreelancer.model.Check
import com.example.libraryoffreelancer.model.Job
import com.example.libraryoffreelancer.model.customJson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class FreelancerViewModel {
    val  client=okhttp3.OkHttpClient()
    val urlRoot="http://10.0.2.2:8000/api/freelancer"
    fun Load_list_job(token: String): List<Job>{
        var listJob : List<Job> = emptyList()
        val req = Request.Builder()
            .url("$urlRoot/load_list_job")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .get()
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                if (response.isSuccessful){
                    listJob = customJson.decodeFromString<List<Job>>(body)
                }
            }
        }
        thread.start()
        thread.join()
        return listJob
    }
//    fun Load_list_job(token: String): Check{
//        var listJob : List<Job>
//        val bodyString= JSONObject()
//            .put("username", account.username)
//            .put("password", account.password)
//            .put("email", account.email)
//            .put("full_name", account.full_name)
//            .put("phone_number", account.phone_number)
//            .toString()
//        val JSON = "application/json; charset=utf-8".toMediaType()
//        val body = bodyString.toRequestBody(JSON)
//        val req = Request.Builder()
//            .url("$urlRoot/register")
//            .post(body)
//            .build()
//        val thread = Thread{
//            client.newCall(req).execute().use { response ->
//                val body = response.body?.string().orEmpty()
//                Log.d("mydebug",body)
//                if (response.isSuccessful){
//                    check.isSuccess=true
//                    check.message = response.message
//                }
//                else{
//                    check.isSuccess = false
//                    check.message = response.message
//                }
//            }
//        }
//        thread.start()
//        thread.join()
//        return check
//    }
}