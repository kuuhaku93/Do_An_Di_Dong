package com.example.libraryoffreelancer.viewmodel

import android.util.Log
import com.example.libraryoffreelancer.model.APIResponse
import com.example.libraryoffreelancer.model.Account
import com.example.libraryoffreelancer.model.Check
import com.example.libraryoffreelancer.model.LoginResult
import com.example.libraryoffreelancer.model.customJson
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private val json = Json{
    ignoreUnknownKeys = true
}
class AccountViewModel {
    val client= OkHttpClient()
    val urlRoot="http://10.0.2.2:8000/api"
    //username: String, password: String, email: String, full_name: String, phone_number: String
    fun Register(account: Account): Check{
        var check = Check(false, "")
        val bodyString= JSONObject()
            .put("username", account.username)
            .put("password", account.password)
            .put("email", account.email)
            .put("full_name", account.full_name)
            .put("phone_number", account.phone_number)
            .toString()
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = bodyString.toRequestBody(JSON)
        val req = Request.Builder()
            .url("$urlRoot/register")
            .post(body)
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
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
    fun Login(username:String,password:String): LoginResult {
        var result = LoginResult(false, "")
        val bodyString= JSONObject()
            .put("username",username)
            .put("password",password)
            .toString()

        val JSON= "application/json; charset=utf-8".toMediaType()
        val body= bodyString.toRequestBody(JSON)

        val req= Request.Builder()
            .url("$urlRoot/login")
            .addHeader("Content-Type","application/json")
            .post(body)
            .build()

        val thread= Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug", body)
                val data = json.decodeFromString<LoginResult>(body)
                Log.d("mydebug", data.toString())
                Log.d("mydebug", data.token.toString())
                if (response.isSuccessful) {
                    result.isSuccess = true
                    result.message = "Đăng nhập thành công"
                    result.token=data.token
                    result.freelancer_status = data.freelancer_status
                    result.employer_status = data.employer_status
                } else {
                    result.isSuccess = false
                    result.message = data.message
                }
            }
        }
        thread.start()
        thread.join()
        return result
    }

    fun Send_otp(email:String): APIResponse {
        var result = APIResponse(false, "")
        val bodyString= JSONObject()
            .put("email",email)
            .toString()

        val JSON= "application/json; charset=utf-8".toMediaType()
        val body= bodyString.toRequestBody(JSON)

        val req= Request.Builder()
            .url("$urlRoot/send_otp")
            .addHeader("Content-Type","application/json")
            .post(body)
            .build()

        val thread= Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug", body)
                result = customJson.decodeFromString<APIResponse>(body)
            }
        }
        thread.start()
        thread.join()
        return result
    }

    fun Check_otp(otp:String): APIResponse {
        var result = APIResponse(false, "")
        val bodyString= JSONObject()
            .put("otp",otp)
            .toString()

        val JSON= "application/json; charset=utf-8".toMediaType()
        val body= bodyString.toRequestBody(JSON)

        val req= Request.Builder()
            .url("$urlRoot/check_otp")
            .addHeader("Content-Type","application/json")
            .post(body)
            .build()

        val thread= Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug", body)
                result = customJson.decodeFromString<APIResponse>(body)
            }
        }
        thread.start()
        thread.join()
        return result
    }

    fun Change_password(otp:String,new_password: String): APIResponse {
        var result = APIResponse(false, "")
        val bodyString= JSONObject()
            .put("otp",otp)
            .put("new_password",new_password)
            .toString()

        val JSON= "application/json; charset=utf-8".toMediaType()
        val body= bodyString.toRequestBody(JSON)

        val req= Request.Builder()
            .url("$urlRoot/change_password")
            .addHeader("Content-Type","application/json")
            .post(body)
            .build()

        val thread= Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug", body)
                result = customJson.decodeFromString<APIResponse>(body)
            }
        }
        thread.start()
        thread.join()
        return result
    }
}