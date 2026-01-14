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
        val btn_back1 = findViewById<ImageButton>(R.id.btn_back1)
        val btn_dang_xuat = findViewById<Button>(R.id.btn_dang_xuat)
        val btn_vai_tro = findViewById<Button>(R.id.btn_vai_tro)
        val btn_lich_su = findViewById<Button>(R.id.btn_lich_su)
        val token = intent.getStringExtra("token")

        btn_dang_xuat.setOnClickListener {
            val settingsViewModel = SettingsViewModel()
            if (!token.isNullOrEmpty()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Thông báo")
                builder.setMessage("Xác nhận đăng xuất?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    val logout = settingsViewModel.Logout(token)
                    if (logout.isSuccess) {
                        startActivity(Intent(applicationContext, DangNhapActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Lỗi không xác định", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}