package com.example.libraryoffreelancer.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.viewmodel.AccountViewModel

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
            if (edt_maOtp.text.toString().isEmpty()){
                Toast.makeText(this, "Bạn chưa nhập mã OTP", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (edt_maOtp.text.toString().length!=6) {
                Toast.makeText(this, "Mã OTP không hợp lệ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val accountViewModel = AccountViewModel()
            val res=accountViewModel.Check_otp(edt_maOtp.text.toString())
            if(res.success){
                Toast.makeText(this, res.message, Toast.LENGTH_LONG).show()
                val chuyen_trang= Intent(this, TaoMatKhauMoiActivity::class.java)
                chuyen_trang.putExtra("otp",edt_maOtp.text.toString())
                startActivity(chuyen_trang)
            }
            else{
                Toast.makeText(this, res.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}