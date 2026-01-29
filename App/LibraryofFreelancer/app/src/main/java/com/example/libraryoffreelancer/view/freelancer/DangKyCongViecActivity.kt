package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel

class DangKyCongViecActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
        val max_salary=intent.getDoubleExtra("max_salary",0.0)
        val min_salary=intent.getDoubleExtra("min_salary",0.0)
        val seekBar = findViewById<SeekBar>(R.id.sb_mucLuong_trangDangKy_freelancer)
        seekBar.max = max_salary.toInt()
        seekBar.min = min_salary.toInt()
        seekBar.progress=(max_salary-min_salary).toInt()
        val txt_max=findViewById<TextView>(R.id.txt_sbMax_dangKyApply)
        txt_max.text=max_salary.toString()
        val txt_min=findViewById<TextView>(R.id.txt_sbMin_dangKyApply)
        txt_min.text=min_salary.toString()
        val txt_mucLuong=findViewById<TextView>(R.id.txt_mucLuong_dangKyApply_freelancer)
        val freelancerViewModel= FreelancerViewModel()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()

        val btn_QuayLaiTrangChiTietCongViec=findViewById<ImageButton>(R.id.btn_QuayLaiTrangChiTietCongViec)
        btn_QuayLaiTrangChiTietCongViec.setOnClickListener {
            this.finish()
        }
        txt_mucLuong.setText(seekBar.progress.toString())
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txt_mucLuong.text = "$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val txt_motacongviec=findViewById<TextView>(R.id.txt_motacongviec)
        val btn_ungtuyen=findViewById<Button>(R.id.btn_ungtuyen)
        btn_ungtuyen.setOnClickListener {
            if(txt_mucLuong.text.toString().isEmpty()){
                Toast.makeText(this, "Bạn chưa nhập mức lương mong muốn", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val apiResponse=freelancerViewModel.Apply_job(job_id,token,  txt_mucLuong.text.toString().toDouble(),txt_motacongviec.text.toString())
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