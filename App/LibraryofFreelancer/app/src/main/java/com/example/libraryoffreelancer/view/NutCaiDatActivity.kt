package com.example.libraryoffreelancer.view

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
import com.example.libraryoffreelancer.view.freelancer.LichSuCongViecActivity
import com.example.libraryoffreelancer.viewmodel.SettingsViewModel

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
        val btn_QuayLaiTrangHoSo=findViewById<ImageButton>(R.id.btn_QuayLaiTrangHoSo)
        btn_QuayLaiTrangHoSo.setOnClickListener {
            finish()
        }
        val btn_dang_xuat=findViewById<Button>(R.id.btn_dang_xuat)
        val btn_vai_tro=findViewById<Button>(R.id.btn_vai_tro)
        val btn_lich_su=findViewById<Button>(R.id.btn_lich_su)
        btn_lich_su.setOnClickListener {
            val intent = Intent(this, LichSuCongViecActivity::class.java)
            startActivity(intent)
        }
    }
}