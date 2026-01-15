package com.example.libraryoffreelancer.view.freelancer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.ChinhSuaHoSoFreelancerActivity
import com.example.libraryoffreelancer.view.NutCaiDatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HoSoFreelancerActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trang_ho_so_employer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btn_CaiDat = findViewById<ImageButton>(R.id.btn_CaiDat)
        btn_CaiDat.setOnClickListener {
            startActivity(Intent(applicationContext, NutCaiDatActivity::class.java))
        }
        val ChinhSua = findViewById<ImageButton>(R.id.btn_ChinhSuaHoSo)
        ChinhSua.setOnClickListener {
            startActivity(Intent(applicationContext, ChinhSuaHoSoFreelancerActivity::class.java))
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.nav_profile
        val btn_caiDat = findViewById<ImageButton>(R.id.btn_settings)



        btn_caiDat.setOnClickListener {
            val intent = Intent(this, NutCaiDatActivity::class.java)
            startActivity(intent)
        }
        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_profile) {
                return@setOnItemSelectedListener true
            }
            when (item.itemId) {
                R.id.nav_applied_job -> {
                    startActivity(Intent(applicationContext, CongViecDaNhanActivity::class.java))
                    overrideActivityTransition(
                        OVERRIDE_TRANSITION_OPEN,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left
                    )
                    finish()
                    true
                }
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, TrangChuFreeLancerActivity::class.java))
                    overrideActivityTransition(
                        OVERRIDE_TRANSITION_OPEN,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left
                    )
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}