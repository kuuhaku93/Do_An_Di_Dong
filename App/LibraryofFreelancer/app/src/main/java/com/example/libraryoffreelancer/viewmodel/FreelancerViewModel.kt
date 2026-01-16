package com.example.libraryoffreelancer.viewmodel

import android.util.Log
import com.example.libraryoffreelancer.model.Account
import com.example.libraryoffreelancer.model.Check
import com.example.libraryoffreelancer.model.Job
import com.example.libraryoffreelancer.model.ListJobsRespone
import com.example.libraryoffreelancer.model.ListRatingResponse
import com.example.libraryoffreelancer.model.LoadPortfolioApiResponse
import com.example.libraryoffreelancer.model.Portfolio
import com.example.libraryoffreelancer.model.Rating
import com.example.libraryoffreelancer.model.customJson
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class FreelancerViewModel {
    val  client=okhttp3.OkHttpClient()
    val urlRoot="http://10.0.2.2:8000/api/freelancer"
    fun Load_list_job(token: String): List<Job>{
        Log.d("mydebug",token)
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
                    val listJobsRespone = customJson.decodeFromString<ListJobsRespone>(body)
                    if (listJobsRespone.success) {
                        listJob = listJobsRespone.jobs
                    }
                    else{
                        Log.d("mydebug",listJobsRespone.message)
                    }
                }
                else{
                    Log.d("mydebug",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return listJob
    }
    fun Load_portfolio(freelancer_id: Int,token: String): Portfolio{
        var portfolio : Portfolio= Portfolio("","","","","",0.0,0.0,emptyList(), emptyList())
        val bodyString= JSONObject()
            .put("freelancer_id", freelancer_id)
            .toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/load_portfolio")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .post(body)
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                val loadPortfolioApiResponse = customJson.decodeFromString<LoadPortfolioApiResponse>(body)
                if (response.isSuccessful){
                    if (loadPortfolioApiResponse.success) {
                        portfolio = loadPortfolioApiResponse.portfolio
                    }
                    else{
                        Log.d("mydebug",loadPortfolioApiResponse.message)
                    }
                }
                else{
                    Log.d("mydebug",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return portfolio
    }
    fun Load_rating(freelancer_id: Int,token: String): List<Rating>{
        var listRating : List<Rating> = emptyList()
        val bodyString= JSONObject()
            .put("freelancer_id", freelancer_id)
            .toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/load_rating")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .post(body)
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                val listRatingResponse = customJson.decodeFromString<ListRatingResponse>(body)
                if (response.isSuccessful){
                    if (listRatingResponse.success) {
                        listRating = listRatingResponse.ratings
                    }
                    else{
                        Log.d("mydebug",listRatingResponse.message)
                    }
                }
                else{
                    Log.d("mydebug",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return listRating
    }
}