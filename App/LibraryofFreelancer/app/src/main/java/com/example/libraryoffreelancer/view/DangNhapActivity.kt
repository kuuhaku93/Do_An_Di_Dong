package com.example.libraryoffreelancer.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.freelancer.TrangChuFreeLancerActivity
import com.example.libraryoffreelancer.viewmodel.AccountViewModel
import androidx.core.content.edit

class DangNhapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dang_nhap)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edt_tai_khoan=findViewById<EditText>(R.id.edt_tai_khoan)
        val edt_mat_khau=findViewById<EditText>(R.id.edt_mat_khau)
        val spn_vai_tro=findViewById<Spinner>(R.id.spn_vai_tro)
        val vai_tro = arrayOf("Freelancer", "Employer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vai_tro)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_vai_tro.adapter = adapter

        val txt_quen_mat_khau=findViewById<TextView>(R.id.txt_quen_mat_khau)
        txt_quen_mat_khau.setOnClickListener {
            val chuyen_trang= Intent(this, QuenMatKhauActivity::class.java)
            startActivity(chuyen_trang)
        }

        val btn_dang_nhap=findViewById<Button>(R.id.btn_dangNhap)
        btn_dang_nhap.setOnClickListener {
            val taiKhoan = edt_tai_khoan.text.toString()
            val matKhau = edt_mat_khau.text.toString()
            val vaitrodachon = spn_vai_tro.selectedItem.toString()
            if (taiKhoan.isEmpty()||matKhau.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val accountViewModel = AccountViewModel()
            Log.d("mydebug", taiKhoan)
            Log.d("mydebug", matKhau)
            val login = accountViewModel.Login(taiKhoan, matKhau)
            Log.d("mydebug", login.toString())
            Log.d("mydebug", login.message)
            if (login.isSuccess) {
                Log.d("mydebug", login.toString())
                val token = login.token
                when (vaitrodachon) {
                    "Freelancer" -> {
                        if (login.freelancer_status == true) {
                            val intent = Intent(this, TrangChuFreeLancerActivity::class.java)
                            val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                            sharedPref.edit {
                                putString("token", token)
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Vai trò Freelancer của tài khoản tạm thời bị khoá!", Toast.LENGTH_LONG).show()
                        }
                    }
                    "Employer" -> {
                        if (login.employer_status == true) {
                            finish()
                        } else {
                            Toast.makeText(this, "Vai trò Employer của tài khoản tạm thời bị khoá!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, login.message, Toast.LENGTH_LONG).show()
            }
        }

        val txt_dang_ky=findViewById<TextView>(R.id.txt_dang_ky)
        txt_dang_ky.setOnClickListener {
            val chuyen_trang= Intent(this, DangKyActivity::class.java)
            startActivity(chuyen_trang)
        }
    }
}