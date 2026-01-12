package com.example.libraryoffreelancer.view

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R

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
        val btn_back1=findViewById<ImageButton>(R.id.btn_back1)
        val btn_dang_xuat=findViewById<Button>(R.id.btn_dang_xuat)
        val btn_vai_tro=findViewById<Button>(R.id.btn_vai_tro)
        val btn_lich_su=findViewById<Button>(R.id.btn_lich_su)
    }
}