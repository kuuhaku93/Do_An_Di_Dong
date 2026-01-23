package com.example.libraryoffreelancer.viewmodel

import android.util.Log
import com.example.libraryoffreelancer.model.CreateJobResponse
import com.example.libraryoffreelancer.model.EmployerJob
import com.example.libraryoffreelancer.model.EmployerJobResponse
import com.example.libraryoffreelancer.model.EmployerProfile
import com.example.libraryoffreelancer.model.EmployerProfileResponse
import com.example.libraryoffreelancer.model.EmployerReview
import com.example.libraryoffreelancer.model.EmployerReviewResponse
import com.example.libraryoffreelancer.model.Job
import com.example.libraryoffreelancer.model.customJson
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private val json = Json{
    ignoreUnknownKeys = true
    coerceInputValues = true
}
class EmployerViewModel {
    val client= OkHttpClient()
    val urlRoot="http://10.0.2.2:8000/api"

    fun loadTrangChuEmployer(token: String): List<EmployerJob>{
        Log.d("mydebug",token)
        var listCongViec : List<EmployerJob> = emptyList()
        val req = Request.Builder()
            .url("$urlRoot/employer/load_list_job")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .get()
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                if (response.isSuccessful){
                    val employerListJobResponse = customJson.decodeFromString<EmployerJobResponse>(body)
                    if (employerListJobResponse.success) {
                        listCongViec = employerListJobResponse.jobs
                    }
                    else{
                        Log.d("mydebug",employerListJobResponse.message)
                    }
                }
                else{
                    Log.d("mydebug",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return listCongViec
    }

    fun loadProfileEmployer(token: String, employerId: Int): EmployerProfile {
        var profile : EmployerProfile= EmployerProfile("","","","","","","", 0.0)
        val bodyString = JSONObject()
            .put("employer_id", employerId).toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)

        val req = Request.Builder()
            .url("$urlRoot/employer/load_profile")
            .addHeader("Authorization", "Token $token")
            .post(body)
            .build()

        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                val loadProfile = json.decodeFromString<EmployerProfileResponse>(body)
                if (response.isSuccessful) {
                    if (loadProfile.success) {
                        profile = loadProfile.profile
                    }else{
                        Log.d("mydebugE",loadProfile.message)
                    }
                }else{
                    Log.d("mydebugE",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return profile
    }
    fun loadEmployerReviews(token: String, employerId: Int): List<EmployerReview>{
        var listReview : List<EmployerReview> = emptyList()
        val jsonBody = JSONObject()
        jsonBody.put("employer_id", employerId)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toString().toRequestBody(mediaType)

        val req = Request.Builder()
            .url("$urlRoot/employer/load_review")
            .addHeader("Authorization", "Token $token")
            .post(body)
            .build()

        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string()
                if (response.isSuccessful && body != null) {
                    Log.d("mydebugE",response.message)
                    val res = json.decodeFromString<EmployerReviewResponse>(body)
                    if(res.success){
                        listReview = res.reviews
                    }
                }else{
                    Log.d("mydebugE",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return listReview
    }
}