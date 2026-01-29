package com.example.libraryoffreelancer.viewmodel

import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.libraryoffreelancer.model.Check
import com.example.libraryoffreelancer.model.Item
import com.example.libraryoffreelancer.model.Job
import com.example.libraryoffreelancer.model.ListItemResponse
import com.example.libraryoffreelancer.model.ListJobsRespone
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.libraryoffreelancer.model.ListSkillsRespone
import com.example.libraryoffreelancer.model.TypeSkill
import com.example.libraryoffreelancer.model.customJson
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun dateconvert(input: String): String {

    val instant = Instant.parse(input)
    val zoneId = ZoneId.of("Asia/Ho_Chi_Minh")
    val zonedDateTime = instant.atZone(zoneId)
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")
    val output = zonedDateTime.format(formatter)
    return output
}
fun pickDateTime(manager: FragmentManager, textView: TextView) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Chọn ngày bắt đầu")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()
    datePicker.addOnPositiveButtonClickListener { selection ->

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText("Chọn giờ")
            .setHour(8)
            .setMinute(0)
            .build()

        timePicker.addOnPositiveButtonClickListener {

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selection

            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            calendar.set(Calendar.SECOND, 0)

            val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault())
            val finalString = sdf.format(calendar.time)

            textView.text = finalString
        }

        timePicker.show(manager, "TIME_PICKER")
    }
    datePicker.show(manager, "DATE_PICKER")
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
    fun Load_list_item(token: String): List<Item>{
        var listTypeItem : List<Item> = emptyList()
        val req = Request.Builder()
            .url("$urlRoot/load_list_item")
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Token $token")
            .get()
            .build()
        val thread = Thread{
            client.newCall(req).execute().use { response ->
                val body = response.body?.string().orEmpty()
                Log.d("mydebug",body)
                if (response.isSuccessful){
                    val listItemsRespone = customJson.decodeFromString<ListItemResponse>(body)
                    if (listItemsRespone.success) {
                        listTypeItem = listItemsRespone.items
                    }
                    else{
                        Log.d("mydebug",listItemsRespone.message)
                    }
                }
                else{
                    Log.d("mydebug",response.message)
                }
            }
        }
        thread.start()
        thread.join()
        return listTypeItem
    }

}