package com.example.libraryoffreelancer.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.employer.LichSuCongViecEmployerActivity
import com.example.libraryoffreelancer.view.employer.TrangChuEmployerActivity
import com.example.libraryoffreelancer.view.freelancer.LichSuCongViecActivity
import com.example.libraryoffreelancer.view.freelancer.TrangChuFreeLancerActivity
import com.example.libraryoffreelancer.viewmodel.AccountViewModel

class NutCaiDatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nut_cai_dat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")
        val is_freelancer=intent.getBooleanExtra("is_freelancer",false)



        val btn_QuayLaiTrangHoSo=findViewById<ImageButton>(R.id.btn_QuayLaiTrangHoSo)
        btn_QuayLaiTrangHoSo.setOnClickListener {
            finish()
        }
        val btn_dang_xuat = findViewById<Button>(R.id.btn_dang_xuat)
        btn_dang_xuat.setOnClickListener {
            val accountViewModel = AccountViewModel()
            val apiResponse = accountViewModel.Logout(token)
            if (apiResponse.success) {
                Toast.makeText(this, apiResponse.message, Toast.LENGTH_SHORT).show()
                sharedPref.edit().clear().apply()
                val intent = Intent(this, DangNhapActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, apiResponse.message, Toast.LENGTH_SHORT).show()
            }

        }

        // Chặn nút Back


        val btn_vai_tro=findViewById<Button>(R.id.btn_vai_tro)
        btn_vai_tro.setOnClickListener {
            if(is_freelancer){
                val intent = Intent(this, TrangChuEmployerActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, TrangChuFreeLancerActivity::class.java)
                startActivity(intent)
            }
        }
        val btn_lich_su=findViewById<Button>(R.id.btn_lich_su)
        btn_lich_su.setOnClickListener {
            if(is_freelancer){
                val intent = Intent(this, LichSuCongViecActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, LichSuCongViecEmployerActivity::class.java)
                startActivity(intent)
            }

        }
    }
}