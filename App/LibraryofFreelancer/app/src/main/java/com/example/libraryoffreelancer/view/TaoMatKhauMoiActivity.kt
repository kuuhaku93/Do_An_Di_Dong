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

class TaoMatKhauMoiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tao_mat_khau_moi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edt_matKhauMoi=findViewById<EditText>(R.id.edt_mat_khau_moi)
        val edt_xacNhanMatKhau=findViewById<EditText>(R.id.edt_xac_nhan_mat_khau)
        val btn_xacNhan=findViewById<Button>(R.id.btn_xac_nhan)
        btn_xacNhan.setOnClickListener {
            if (edt_matKhauMoi.text.toString().isEmpty()||edt_xacNhanMatKhau.text.toString().isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (edt_matKhauMoi.text.toString()!=edt_xacNhanMatKhau.text.toString()){
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val otp= this.intent.getStringExtra("otp")
            val accountViewModel = AccountViewModel()
            val res=accountViewModel.Change_password(otp.toString(),edt_matKhauMoi.text.toString())
            if(res.success){
                val chuyem_trang= Intent(this, DangNhapActivity::class.java)
                startActivity(chuyem_trang)
                Toast.makeText(this, res.message, Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, res.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}