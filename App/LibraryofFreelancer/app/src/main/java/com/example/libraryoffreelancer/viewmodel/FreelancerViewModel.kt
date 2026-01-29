package com.example.libraryoffreelancer.viewmodel

import android.util.Log
import com.example.libraryoffreelancer.model.APIResponse
import com.example.libraryoffreelancer.model.Account
import com.example.libraryoffreelancer.model.Application
import com.example.libraryoffreelancer.model.Check
import com.example.libraryoffreelancer.model.CurrentJob
import com.example.libraryoffreelancer.model.CustomItem
import com.example.libraryoffreelancer.model.HistoryJob
import com.example.libraryoffreelancer.model.ItemRequest
import com.example.libraryoffreelancer.model.Job
import com.example.libraryoffreelancer.model.ListApplicationsRespone
import com.example.libraryoffreelancer.model.ListCurrent
import com.example.libraryoffreelancer.model.ListHistory
import com.example.libraryoffreelancer.model.ListJobsRespone
import com.example.libraryoffreelancer.model.ListRatingResponse
import com.example.libraryoffreelancer.model.LoadPortfolioApiResponse
import com.example.libraryoffreelancer.model.Portfolio
import com.example.libraryoffreelancer.model.Rating
import com.example.libraryoffreelancer.model.customJson
import kotlinx.serialization.builtins.ListSerializer
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
    fun Load_list_job_by_search(token: String,keyword: String,requirement: List<Int>): List<Job>{
        Log.d("mydebug",token)
        var listJob : List<Job> = emptyList()
        val bodyString= JSONObject()
            .put("keyword", keyword)
            .put("requirement", requirement)
            .toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/load_list_job_by_search")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .post(body)
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
    fun Apply_job(job_id: Int, token: String, wanted_salary: Double, description:String): APIResponse {
        var apiResponse: APIResponse = APIResponse(false,"")
        val bodyString= JSONObject()
            .put("description", description)
            .put("job_id", job_id)
            .put("wanted_salary", wanted_salary)
            .toString()
        Log.d("mydebug",bodyString)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/apply_job")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .post(body)
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                apiResponse = customJson.decodeFromString<APIResponse>(body)
            }
        }
        thread.start()
        thread.join()
        return apiResponse
    }
    fun Load_history_job(token: String): List<HistoryJob>{
        Log.d("mydebug",token)
        var listJob : List<HistoryJob> = emptyList()
        val req = Request.Builder()
            .url("$urlRoot/load_history_job")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .get()
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                if (response.isSuccessful){
                    val listHistory = customJson.decodeFromString<ListHistory>(body)
                    if (listHistory.success) {
                        listJob = listHistory.jobs
                    }
                    else{
                        Log.d("mydebug",listHistory.message)
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
    fun Load_application(job_id: Int,token: String): List<Application>{
        var listApplication : List<Application> = emptyList()
        val bodyString= JSONObject()
            .put("job_id", job_id)
            .toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("http://10.0.2.2:8000/api/load_list_application")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .post(body)
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                val listApplicationsRespone = customJson.decodeFromString<ListApplicationsRespone>(body)
                if (response.isSuccessful){
                    if (listApplicationsRespone.success) {
                        listApplication = listApplicationsRespone.applications
                    }
                    else{
                        Log.d("mydebug",listApplicationsRespone.message)
                    }
                }
                else{
                    Log.d("mydebug",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return listApplication
    }
    fun Load_current_job(token: String): List<CurrentJob>{
        Log.d("mydebug",token)
        var listJob : List<CurrentJob> = emptyList()
        val req = Request.Builder()
            .url("$urlRoot/load_current_job")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .get()
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                if (response.isSuccessful){
                    val listCurent = customJson.decodeFromString<ListCurrent>(body)
                    if (listCurent.success) {
                        listJob = listCurent.jobs
                    }
                    else{
                        Log.d("mydebug",listCurent.message)
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
    fun Create_Review(contact_id: Int, comment: String, score: Double, token: String): APIResponse{
        var resp= APIResponse(false,"")
        val bodyString= JSONObject()
            .put("contact_id", contact_id)
            .put("comment", comment)
            .put("score", score)
            .toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/create_review")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .post(body)
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                resp = customJson.decodeFromString<APIResponse>(body)

            }
        }
        thread.start()
        thread.join()
        return resp
    }
    fun Edit_Portfolio(token:String,freelancer_name: String,avatar: String,email: String,phone_number: String,description: String,skills: List<Int>,items: List<ItemRequest>,custom:List<CustomItem>): APIResponse{
        var resp= APIResponse(false,"")
        val bodyString= JSONObject()
            .put("freelancer_name", freelancer_name)
            .put("avatar", avatar)
            .put("email", email)
            .put("phone_number", phone_number)
            .put("description", description)
            .put("skills", skills)
            .put("items", Json.encodeToString(ListSerializer(ItemRequest.serializer()),items))
            .put("customs", Json.encodeToString(ListSerializer(CustomItem.serializer()),custom))
            .toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/edit_portfolio")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .post(body)
            .build()
        Log.d("mydebug",bodyString)
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                resp = customJson.decodeFromString<APIResponse>(body)

            }
        }
        thread.start()
        thread.join()
        return resp
    }
}