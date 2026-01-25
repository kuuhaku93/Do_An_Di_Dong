package com.example.libraryoffreelancer.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.libraryoffreelancer.model.Check
import com.example.libraryoffreelancer.model.Job
import com.example.libraryoffreelancer.model.ListJobsRespone
import com.example.libraryoffreelancer.model.ListSkillsRespone
import com.example.libraryoffreelancer.model.TypeSkill
import com.example.libraryoffreelancer.model.customJson
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val json = Json{
    ignoreUnknownKeys = true
}
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun dateconvert(input: String): String {

    val instant = Instant.parse(input)
    val zoneId = ZoneId.of("Asia/Ho_Chi_Minh")
    val zonedDateTime = instant.atZone(zoneId)
    val formatter = DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy")
    val output = zonedDateTime.format(formatter)
    return output
}
class SettingsViewModel {
    val client= OkHttpClient()
    val urlRoot="http://10.0.2.2:8000/api"
    fun Load_list_skill(token: String): List<TypeSkill>{
        var listTypeSkill : List<TypeSkill> = emptyList()
        val req = Request.Builder()
            .url("$urlRoot/load_list_skill")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .get()
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                if (response.isSuccessful){
                    val listSkillsRespone = customJson.decodeFromString<ListSkillsRespone>(body)
                    if (listSkillsRespone.success) {
                        listTypeSkill = listSkillsRespone.skills
                    }
                    else{
                        Log.d("mydebug",listSkillsRespone.message)
                    }
                }
                else{
                    Log.d("mydebug",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return listTypeSkill
    }
}