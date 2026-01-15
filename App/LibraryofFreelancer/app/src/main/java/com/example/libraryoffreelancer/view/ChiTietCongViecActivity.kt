package com.example.libraryoffreelancer.view

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R

class ChiTietCongViecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chi_tiet_cong_viec_employer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_ChiTietCongViec)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txt_JobMatch = findViewById<TextView>(R.id.txt_JobMatch)
        val btn_accept = findViewById<ImageButton>(R.id.btn_accept_ChiTietCongViec)
        val btn_decline = findViewById<ImageButton>(R.id.btn_decline_ChiTietCongViec)
        val txt_Description = findViewById<TextView>(R.id.txt_Description_chiTietCongViec)
        val txt_CompanyName = findViewById<TextView>(R.id.tvCompanyName_ChiTietCongViec)
        val txt_JobTitle = findViewById<TextView>(R.id.txt_jobTitle_ChiTietcongViec)
        val txt_salary = findViewById<TextView>(R.id.tvSalary)
        val txt_location = findViewById<TextView>(R.id.tvLocation)
    }
}