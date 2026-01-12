package com.example.libraryoffreelancer.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R

class NhapMaOTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nhap_ma_otp)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edt_maOtp=findViewById<EditText>(R.id.edt_ma)
        val btn_xacNhanOtp=findViewById<Button>(R.id.btn_xac_nhan_ma)
        btn_xacNhanOtp.setOnClickListener {
            val chuyen_trang= Intent(this, TaoMatKhauMoiActivity::class.java)
            startActivity(chuyen_trang)
        }
    }
}