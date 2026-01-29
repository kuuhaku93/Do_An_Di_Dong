package com.example.libraryoffreelancer.viewmodel

import android.util.Log
import com.example.libraryoffreelancer.model.APIResponse
import com.example.libraryoffreelancer.model.Application
import com.example.libraryoffreelancer.model.CreateContactRequest
import com.example.libraryoffreelancer.model.CreateJobRequest
import com.example.libraryoffreelancer.model.CreateRatingRequest
import com.example.libraryoffreelancer.model.EmployerCurrentJob
import com.example.libraryoffreelancer.model.EmployerCurrentJobResponse
import com.example.libraryoffreelancer.model.EmployerJob
import com.example.libraryoffreelancer.model.EmployerJobHistory
import com.example.libraryoffreelancer.model.EmployerJobResponse
import com.example.libraryoffreelancer.model.EmployerProfile
import com.example.libraryoffreelancer.model.EmployerProfileResponse
import com.example.libraryoffreelancer.model.EmployerReview
import com.example.libraryoffreelancer.model.EmployerReviewResponse
import com.example.libraryoffreelancer.model.ListApplicationsRespone
import com.example.libraryoffreelancer.model.ListJobHistory
import com.example.libraryoffreelancer.model.UpdateProfileRequest
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
    fun updateProfile(token: String, requestData: UpdateProfileRequest): APIResponse {
        var result = APIResponse(false, "Lỗi kết nối")
            val jsonBody = JSONObject()
            jsonBody.put("company_name", requestData.company_name)
            jsonBody.put("phone_number", requestData.phone_number)
            jsonBody.put("website", requestData.website)
            jsonBody.put("address", requestData.address)
            jsonBody.put("employer_description", requestData.employer_description)
            jsonBody.put("email", requestData.email)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonBody.toString().toRequestBody(mediaType)

            val req = Request.Builder()
                .url("$urlRoot/employer/update_profile") // Nhớ thêm URL này vào urls.py của Django
                .addHeader("Authorization", "Token $token")
                .post(body)
                .build()

            client.newCall(req).execute().use { response ->
                val respBody = response.body?.string().orEmpty()
                if (response.isSuccessful) {
                    val jsonRes = JSONObject(respBody)
                    val success = jsonRes.optBoolean("success")
                    val message = jsonRes.optString("message")
                    result = APIResponse(success, message)
                } else {
                    result = APIResponse(false, response.message)
                }
            }
        return result
    }
    fun createRating(token: String, requestData: CreateRatingRequest): APIResponse{
        var result = APIResponse(false,"")
        val bodyString= JSONObject()
            .put("contact_id", requestData.contact_id)
            .put("comment", requestData.comment)
            .put("rating", requestData.rating)
            .put("complete", requestData.complete)
            .toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/create_rating")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .post(body)
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                result = customJson.decodeFromString<APIResponse>(body)
            }
        }
        thread.start()
        thread.join()
        return result
    }

    fun loadCurrentJob(token:String):List<EmployerCurrentJob> {
        var listJobs: List<EmployerCurrentJob> = emptyList()
        val thread = Thread {
            val url = "$urlRoot/employer/load_current_job"
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Token $token")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()
                if (response.isSuccessful) {
                    val listResponse = customJson.decodeFromString<EmployerCurrentJobResponse>(body)
                    if (listResponse.success) {
                        listJobs = listResponse.jobs
                    } else {
                        Log.d("mydebug", listResponse.message)
                    }
                } else {
                    Log.d("mydebug", response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return listJobs
    }
    fun createContact(token: String, requestData: CreateContactRequest): APIResponse {
        var result = APIResponse(false, "Lỗi kết nối")
        val thread = Thread {
            val bodyString = JSONObject()
            bodyString.put("application_id", requestData.application_id)
            bodyString.put("start_date", requestData.start_date)
            bodyString.put("end_date", requestData.end_date)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = bodyString.toString().toRequestBody(mediaType)

            val req = Request.Builder()
                .url("$urlRoot/employer/create_contact")
                .addHeader("Authorization", "Token $token")
                .post(body)
                .build()

            client.newCall(req).execute().use { response ->
                val respBody = response.body?.string().orEmpty()
                if (response.isSuccessful) {
                    if (respBody.isNotEmpty()) {
                        result = customJson.decodeFromString<APIResponse>(respBody)
                    } else {
                        result = APIResponse(false, "Server trả về rỗng: ${response.code}")
                    }
                }
            }
        }
        thread.start()
        thread.join()
        return result
    }
    fun loadJobHistory(token: String): List<EmployerJobHistory>{
        Log.d("mydebug",token)
        var jobList : List<EmployerJobHistory> = emptyList()
        val req = Request.Builder()
            .url("$urlRoot/employer/load_history_job")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .get()
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                if (response.isSuccessful){
                    val listJobHistory = customJson.decodeFromString<ListJobHistory>(body)
                    if (listJobHistory.success) {
                        jobList = listJobHistory.jobs
                    }
                    else{
                        Log.d("mydebug",listJobHistory.message)
                    }
                }
                else{
                    Log.d("mydebug",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return jobList
    }
    fun createJob(token: String, jobData: CreateJobRequest): APIResponse {
        var result = APIResponse(false, "Lỗi kết nối hoặc không phản hồi")

        val thread = Thread {
                val jsonBody = JSONObject()
                jsonBody.put("title", jobData.title)
                jsonBody.put("description", jobData.description)
                jsonBody.put("salary_min", jobData.salaryMin)
                jsonBody.put("salary_max", jobData.salaryMax)
                jsonBody.put("location", jobData.location)
                jsonBody.put("deadline", jobData.deadline)
                jsonBody.put("end_date", jobData.endDate)
                jsonBody.put("max_employee", jobData.maxEmployee)
                jsonBody.put("requirements", jobData.requirements.toString())

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val body = jsonBody.toString().toRequestBody(mediaType)

                val req = Request.Builder()
                    .url("$urlRoot/employer/create_job")
                    .addHeader("Authorization", "Token $token")
                    .post(body)
                    .build()

                client.newCall(req).execute().use { response ->
                    val responseBody = response.body?.string().orEmpty()
                    if (response.isSuccessful) {
                        result = customJson.decodeFromString<APIResponse>(responseBody)
                    } else {
                        Log.d("mydebug", response.message)
                    }
                }
        }
        thread.start()
        thread.join()
        return result
    }
    fun closeJob(token: String, jobId: Int): APIResponse{
        var apiResponse= APIResponse(false,"")
        val bodyString = JSONObject()
            .put("job_id", jobId).toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/employer/close_job")
            .addHeader("Authorization", "Token $token")
            .post(body)
            .build()
        val thread= Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug", body)
                apiResponse = customJson.decodeFromString<APIResponse>(body)
            }
        }
        thread.start()
        thread.join()
        return apiResponse
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
                val loadProfile = customJson.decodeFromString<EmployerProfileResponse>(body)
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