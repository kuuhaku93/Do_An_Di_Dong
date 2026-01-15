package com.example.libraryoffreelancer.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R

class FormThemCongViecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_them_cong_viec)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btn_confirm = findViewById<Button>(R.id.btn_confirm)
        val btn_addSkill = findViewById<TextView>(R.id.chip_add)

        btn_addSkill.setOnClickListener(){
            intent = Intent(this, DanhSachKyNangActivity::class.java)
            startActivity(intent)

        }
        val btn_return = findViewById<TextView>(R.id.txt_return_FormThemCongViec)
        val txt_jobname = findViewById<TextView>(R.id.txt_jobname)
        val txt_luong_toi_thieu = findViewById<TextView>(R.id.txt_luong_toi_thieu)
        val txt_luong_toi_da = findViewById<TextView>(R.id.txt_luong_toi_da)
        val txt_deadline = findViewById<TextView>(R.id.txt_deadline)
        val txt_deadline2 = findViewById<TextView>(R.id.txt_deadline2)
        val txt_moTa_congViec = findViewById<TextView>(R.id.txt_moTa_congViec)
        val txt_viTri_congViec = findViewById<TextView>(R.id.txt_viTri_congViec)
        val txt_position = findViewById<TextView>(R.id.txt_position)



    }
}