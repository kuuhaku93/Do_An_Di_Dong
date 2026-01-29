package com.example.libraryoffreelancer.view.employer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.EmployerCurrentJob
import com.example.libraryoffreelancer.view.adapter.EmployerCurrentJobAdapter

import com.example.libraryoffreelancer.viewmodel.EmployerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CongViecDaDangEmployerActivity : AppCompatActivity(), EmployerCurrentJobAdapter.OnFinishClickListener {
    private val employerViewModel = EmployerViewModel()
    private lateinit var adapter: EmployerCurrentJobAdapter
    private var token: String = " "
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cong_viec_da_dang)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("Pref", Context.MODE_PRIVATE)
        val userID=sharedPref.getInt("ACCOUNT_ID",0)
        token=sharedPref.getString("token","").orEmpty()

        val rev_congViecHienTai = findViewById<RecyclerView>(R.id.rev_congViecHienTai_Employer)
        rev_congViecHienTai.layoutManager = LinearLayoutManager(this)
        adapter = EmployerCurrentJobAdapter(emptyList(), token, this)
        rev_congViecHienTai.adapter = adapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.nav_applied_job

        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_applied_job) {
                return@setOnItemSelectedListener true
            }
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, TrangChuEmployerActivity::class.java))
                    overrideActivityTransition(
                        OVERRIDE_TRANSITION_OPEN,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left
                    )
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, HoSoEmployerActivity::class.java).putExtra("self",true).putExtra("employer_id",userID))
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
    override fun onResume() {
        super.onResume()
        val list = employerViewModel.loadCurrentJob(token)
        adapter.updateData(list)
    }
    override fun onFinishClick(job: EmployerCurrentJob) {
        val intent = Intent(this, DanhGiaFreelancerActivity::class.java)
        intent.putExtra("contact_id", job.contact_id)
        intent.putExtra("freelancer_id", job.freelancer_id)
        startActivity(intent)
    }
}