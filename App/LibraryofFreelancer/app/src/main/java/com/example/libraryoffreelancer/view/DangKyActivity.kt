package com.example.libraryoffreelancer.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.Account
import com.example.libraryoffreelancer.viewmodel.AccountViewModel

class DangKyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_ky)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edt_ho_ten = findViewById<EditText>(R.id.edt_ho_ten)
        val edt_email = findViewById<EditText>(R.id.edt_email)
        val edt_SDT = findViewById<EditText>(R.id.edt_SDT)
        val edt_ten_dang_nhap = findViewById<EditText>(R.id.edt_ten_dang_nhap)
        val edt_mat_khau01 = findViewById<EditText>(R.id.edt_mat_khau01)
        val edt_xac_nhan_mat_khau01 = findViewById<EditText>(R.id.edt_xac_nhan_mat_khau01)
        val btn_dang_ky = findViewById<Button>(R.id.btn_dang_ky)
        btn_dang_ky.setOnClickListener {
            val fullname = edt_ho_ten.text.toString()
            val email = edt_email.text.toString()
            val phonenumber = edt_SDT.text.toString()
            val username = edt_ten_dang_nhap.text.toString()
            val password = edt_mat_khau01.text.toString()
            val repassword = edt_xac_nhan_mat_khau01.text.toString()
            if (fullname.isEmpty()||email.isEmpty()||phonenumber.isEmpty()||username.isEmpty()||password.isEmpty()||repassword.isEmpty()){
                Toast.makeText(
                    this,
                    "Thông tin không được thiếu!",
                    Toast.LENGTH_SHORT
                ).show()
            }else if (!repassword.equals(password)){
                Toast.makeText(
                    this,
                    "Mật khẩu không trùng khớp!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                val accountViewModel = AccountViewModel()
                val newAccount = Account(username, password, email, fullname, phonenumber)
                val check = accountViewModel.Register(newAccount)
                Log.d("md", check.toString())
                if (check.isSuccess){
                    Toast.makeText(
                            this,
                            "Đăng ký thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, DangNhapActivity::class.java)
                        startActivity(intent)
                }else{
                    Toast.makeText(this, check.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}