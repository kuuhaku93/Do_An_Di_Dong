package com.example.libraryoffreelancer.view

import android.annotation.SuppressLint
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

class QuenMatKhauActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quen_mat_khau)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edt_quenMatkhau=findViewById<EditText>(R.id.edt_ma)
        val btn_xacNhan=findViewById<Button>(R.id.btn_xac_nhan_ma)
            btn_xacNhan.setOnClickListener {
                if (edt_quenMatkhau.text.toString().isEmpty()){
                    Toast.makeText(this, "Bạn chưa nhập Email", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val accountViewModel = AccountViewModel()
                val res=accountViewModel.Send_otp(edt_quenMatkhau.text.toString())
                if(res.success){
                    Toast.makeText(this, res.message, Toast.LENGTH_LONG).show()
                    val chuyen_trang= Intent(this, NhapMaOTPActivity::class.java)
                    startActivity(chuyen_trang)
                }
                else{
                    Toast.makeText(this, res.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}