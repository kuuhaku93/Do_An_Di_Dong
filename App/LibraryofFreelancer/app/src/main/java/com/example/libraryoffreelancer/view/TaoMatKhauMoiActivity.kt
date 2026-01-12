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
            val chuyem_trang= Intent(this, NutCaiDatActivity::class.java)
            startActivity(chuyem_trang)
        }
    }
}