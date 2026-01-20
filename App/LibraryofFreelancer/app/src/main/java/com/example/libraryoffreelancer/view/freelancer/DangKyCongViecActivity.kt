package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel

class DangKyCongViecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_ky_cong_viec)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val job_id=intent.getIntExtra("job_id",0)
        val freelancerViewModel= FreelancerViewModel()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()

        val btn_QuayLaiTrangChiTietCongViec=findViewById<ImageButton>(R.id.btn_QuayLaiTrangChiTietCongViec)
        btn_QuayLaiTrangChiTietCongViec.setOnClickListener {
            this.finish()
        }
        val txt_mucluongmongmuon_trangdangkycongviec=findViewById<TextView>(R.id.txt_mucluongmongmuon_trangdangkycongviec)
        val txt_motacongviec=findViewById<TextView>(R.id.txt_motacongviec)
        val btn_ungtuyen=findViewById<Button>(R.id.btn_ungtuyen)
        btn_ungtuyen.setOnClickListener {
            if(txt_mucluongmongmuon_trangdangkycongviec.text.toString().isEmpty()){
                Toast.makeText(this, "Bạn chưa nhập mức lương mong muốn", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val apiResponse=freelancerViewModel.Apply_job(job_id,token,  txt_mucluongmongmuon_trangdangkycongviec.text.toString().toDouble(),txt_motacongviec.text.toString())
            if(apiResponse.success){
                Toast.makeText(this, apiResponse.message, Toast.LENGTH_LONG).show()
                this.finish()
            }
            else{
                Toast.makeText(this, apiResponse.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}