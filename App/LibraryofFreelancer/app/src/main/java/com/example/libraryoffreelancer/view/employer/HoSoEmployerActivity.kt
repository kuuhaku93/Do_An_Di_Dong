package com.example.libraryoffreelancer.view.employer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.EmployerReviewAdapter
import com.example.libraryoffreelancer.view.NutCaiDatActivity
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HoSoEmployerActivity : AppCompatActivity() {
    private lateinit var imgAvatar: ImageView
    private lateinit var txt_tenCongTy: TextView
    private lateinit var txt_moTa: TextView
    private lateinit var txt_Email: TextView
    private lateinit var txt_SDT: TextView
    private lateinit var txt_Website: TextView
    private lateinit var txt_diaChi: TextView
    private lateinit var txt_diemDanhGia: TextView
    private lateinit var rcvDanhGia: RecyclerView

    private val employerViewModel = EmployerViewModel()
    private var token: String = ""
    private var employerId: Int = 0
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ho_so_employer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val is_self = intent.getBooleanExtra("self", false)
        val Pref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val token = Pref.getString("token", "")

        employerId = intent.getIntExtra("employer_id", 0)
        val profile = employerViewModel.loadProfileEmployer(token!!, employerId)

        val imgAvatar = findViewById<ImageView>(R.id.img_company_logo)
        Glide.with(this)
            .load(profile.company_logo)
            .into(imgAvatar)
        txt_tenCongTy = findViewById<TextView>(R.id.txt_companyName)
        txt_moTa = findViewById<TextView>(R.id.txt_mota_trangChuEmployer)
        txt_Email = findViewById<TextView>(R.id.txt_email)
        txt_SDT = findViewById<TextView>(R.id.txt_phone_TrangChuEmployer)
        txt_Website = findViewById<TextView>(R.id.txt_website)
        txt_diaChi = findViewById<TextView>(R.id.txt_diachi)
        txt_diemDanhGia = findViewById<TextView>(R.id.so_sao_review_Employer)

        txt_tenCongTy.text = profile.company_name
        txt_moTa.text = profile.employer_description
        txt_Email.text = profile.email
        txt_SDT.text = profile.phone_number
        txt_Website.text = profile.website
        txt_diaChi.text = profile.address
        txt_diemDanhGia.text = profile.rating.toString()

        rcvDanhGia = findViewById<RecyclerView>(R.id.rcv_danh_gia_Employer)
        val listRating = employerViewModel.loadEmployerReviews(token, employerId)
        var reviewAdapter = EmployerReviewAdapter(listRating)
        rcvDanhGia.layoutManager = LinearLayoutManager(this)
        rcvDanhGia.adapter = reviewAdapter

        val btn_chinhSua = findViewById<ImageButton>(R.id.btn_ChinhSuaHoSoEmployer)
        val btn_caidat = findViewById<ImageButton>(R.id.btn_CaiDat_Employer)
        val btn_back = findViewById<ImageButton>(R.id.btn_back_profile_employer)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.nav_profile
        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_profile) {
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
                else -> false
            }
        }
        btn_back.setOnClickListener {
            this.finish()
        }
        btn_caidat.setOnClickListener {
            val intent = Intent(this, NutCaiDatActivity::class.java)
            intent.putExtra("is_freelancer", false)
            startActivity(intent)
        }
        btn_chinhSua.setOnClickListener {
            val intent = Intent(this, ChinhSuaHoSoEmployerActivity::class.java)
            startActivity(intent)
        }
        if (is_self) {
            btn_chinhSua.visibility = View.VISIBLE
            btn_caidat.visibility = View.VISIBLE
            btn_back.visibility = View.GONE
            bottomNavigationView.visibility = View.VISIBLE
        }
        else{
            bottomNavigationView.visibility = View.GONE
            btn_chinhSua.visibility = View.GONE
            btn_caidat.visibility = View.GONE
            btn_back.visibility = View.VISIBLE
        }
    }
    override fun onResume() {
        super.onResume()
        val profile = employerViewModel.loadProfileEmployer(token, employerId)
        txt_tenCongTy.text = profile.company_name
        txt_moTa.text = profile.employer_description
        txt_Email.text = profile.email
        txt_SDT.text = profile.phone_number
        txt_Website.text = profile.website
        txt_diaChi.text = profile.address
        txt_diemDanhGia.text = profile.rating.toString()
        Glide.with(this@HoSoEmployerActivity)
            .load(profile.company_logo)
            .placeholder(R.drawable.baseline_account_circle_24)
            .error(R.drawable.baseline_account_circle_24)
            .into(imgAvatar)
        val list = employerViewModel.loadEmployerReviews(token, employerId)
        val reviewAdapter = EmployerReviewAdapter(list)
        rcvDanhGia.layoutManager = LinearLayoutManager(this)
        rcvDanhGia.adapter = reviewAdapter
    }
}