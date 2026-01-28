package com.example.libraryoffreelancer.view.freelancer

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
import com.example.libraryoffreelancer.view.adapter.CurrentJobAdapter
import com.example.libraryoffreelancer.view.employer.HoSoEmployerActivity
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CongViecDaNhanActivity : AppCompatActivity(), CurrentJobAdapter.AvatarEmployerClick {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cong_viec_hien_tai_freelancer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()
        val userID=sharedPref.getInt("ACCOUNT_ID",0)

        val rev_congViecHienTai=findViewById<RecyclerView>(R.id.rev_congViecHienTai)
        rev_congViecHienTai.layoutManager= LinearLayoutManager(this)
        val freelancerViewHolder= FreelancerViewModel()
        rev_congViecHienTai.adapter= CurrentJobAdapter(freelancerViewHolder.Load_current_job(token),token,this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.nav_applied_job
        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_applied_job) {
                return@setOnItemSelectedListener true
            }
            when (item.itemId) {
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
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, HoSoFreelancerActivity::class.java).putExtra("self",true).putExtra("freelancer_id",userID))
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

    override fun onItemClick(employer_id: Int) {
        val intent= Intent(this, HoSoEmployerActivity::class.java)
        intent.putExtra("employer_id",employer_id)
        intent.putExtra("self",false)
        startActivity(intent)
    }

    override fun onDanhGiaClick(contact_id: Int, job_title: String, company_name: String) {
        val intent= Intent(this, DanhGiaEmloyerActivity::class.java)
        intent.putExtra("contact_id",contact_id)
        intent.putExtra("job_title",job_title)
        intent.putExtra("company_name",company_name)
        startActivity(intent)
    }
}