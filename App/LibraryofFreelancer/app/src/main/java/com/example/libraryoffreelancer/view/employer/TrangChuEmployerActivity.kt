package com.example.libraryoffreelancer.view.employer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.OnItemClickListener
import com.example.libraryoffreelancer.view.adapter.TrangChuEmployerAdapter
import com.example.libraryoffreelancer.view.freelancer.CongViecDaNhanActivity
import com.example.libraryoffreelancer.view.freelancer.HoSoFreelancerActivity
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class TrangChuEmployerActivity : AppCompatActivity(), TrangChuEmployerAdapter.OnJobClickListener {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trang_chu_employer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("Pref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()
        val rev_danhSachCongViec = findViewById<RecyclerView>(R.id.rcv_CongViecEmployer)
        rev_danhSachCongViec.layoutManager = LinearLayoutManager(this)
        val employerViewModel = EmployerViewModel()
        rev_danhSachCongViec.adapter = TrangChuEmployerAdapter(employerViewModel.loadTrangChuEmployer(token), this)

        val btn_them = findViewById<ImageButton>(R.id.btn_themCongViec).setOnClickListener {
            val intent = Intent(this, FormThemCongViecActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_home) {
                return@setOnItemSelectedListener true
            }
            when (item.itemId) {
                R.id.nav_applied_job -> {
                    startActivity(Intent(applicationContext, CongViecDaDangEmployerActivity::class.java))
                    overrideActivityTransition(
                        OVERRIDE_TRANSITION_OPEN,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left
                    )
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, HoSoEmployerActivity::class.java))
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

    override fun onJobClick(position: Int) {
        val intent = Intent(this, ChiTietCongViecActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}