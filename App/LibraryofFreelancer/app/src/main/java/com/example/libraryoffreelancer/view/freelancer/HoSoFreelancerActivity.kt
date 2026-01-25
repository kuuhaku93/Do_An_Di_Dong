package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.freelancer.ChinhSuaHoSoFreelancerActivity
import com.example.libraryoffreelancer.view.NutCaiDatActivity
import com.example.libraryoffreelancer.view.adapter.PortfolioAdapter
import com.example.libraryoffreelancer.view.adapter.PortfolioSkillAdapter
import com.example.libraryoffreelancer.view.adapter.RatingAdapter
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HoSoFreelancerActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ho_so_freelancer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val is_self = intent.getBooleanExtra("self", false)
        val ChinhSua = findViewById<ImageButton>(R.id.btn_ChinhSuaHoSoFreelancer)
        ChinhSua.setOnClickListener {
            startActivity(Intent(applicationContext, ChinhSuaHoSoFreelancerActivity::class.java))
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.nav_profile
        val btn_caiDat = findViewById<ImageButton>(R.id.btn_CaiDat_Freelancer)
        btn_caiDat.setOnClickListener {
            val intent = Intent(this, NutCaiDatActivity::class.java)
            startActivity(intent)
        }
        val btn_back=findViewById<ImageButton>(R.id.btn_back_portfolio_freelancer)
        btn_back.setOnClickListener {
            this.finish()
        }

 //Load thông tin freelancer
        val freelancerViewModel= FreelancerViewModel()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")
        val freelancer_id = intent.getIntExtra("freelancer_id", 0)
        val portfolio = freelancerViewModel.Load_portfolio(freelancer_id,token!!)
        val image_anhdaidien = findViewById<ImageView>(R.id.image_anhdaidien)
        Glide.with(this )
            .load(portfolio.avatar)
            .into(image_anhdaidien)
        val txt_ho_ten = findViewById<TextView>(R.id.txt_HovaTen)
        txt_ho_ten.text = portfolio.freelancer_name
        val txt_mota = findViewById<TextView>(R.id.txt_mota)
        txt_mota.text = portfolio.description
        val txt_email = findViewById<TextView>(R.id.txt_email)
        txt_email.text = portfolio.email
        val txt_sdt = findViewById<TextView>(R.id.txt_sdt)
        txt_sdt.text = portfolio.phone_number
        val txt_ty_le = findViewById<TextView>(R.id.txt_ty_le)
        txt_ty_le.text = (portfolio.complete*100).toString() + "%"
        val txt_so_danh_gia = findViewById<TextView>(R.id.so_danh_gia)
        txt_so_danh_gia.text = portfolio.rating.toString()
        val txt_so_sao_review = findViewById<TextView>(R.id.so_sao_review)
        txt_so_sao_review.text = portfolio.rating.toString()
        val rev_kynang = findViewById<RecyclerView>(R.id.rcv_kynang)
        rev_kynang.layoutManager= GridLayoutManager(this,3)
        val skillsList = mutableListOf<String>()
        for (map in portfolio.skills) {
            for ((_, list) in map) {
                if (!list.isNullOrEmpty()) {
                    skillsList += list.map { it.trim() }.filter { it.isNotEmpty() }
                }
            }
        }
        rev_kynang.adapter = PortfolioSkillAdapter(skillsList)
        val rev_thongtin = findViewById<RecyclerView>(R.id.rcv_thongtin)
        rev_thongtin.layoutManager= LinearLayoutManager(this)
        rev_thongtin.adapter = PortfolioAdapter(portfolio.items)
        val listRating=freelancerViewModel.Load_rating(freelancer_id,token)
        val rev_danh_gia = findViewById<RecyclerView>(R.id.rcv_danh_gia)
        rev_danh_gia.layoutManager= LinearLayoutManager(this)
        rev_danh_gia.adapter = RatingAdapter(listRating)
        val txt_so_cong_viec = findViewById<TextView>(R.id.so_cong_viec)
        txt_so_cong_viec.text = listRating.size.toString()
        val txt_so_review = findViewById<TextView>(R.id.so_review)
        txt_so_review.text = "${listRating.size} review"
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
        if (is_self) {
            ChinhSua.visibility = View.VISIBLE
            btn_caiDat.visibility = View.VISIBLE
            btn_back.visibility = View.GONE
            bottomNavigationView.visibility = View.VISIBLE
        }
        else{
            bottomNavigationView.visibility = View.GONE
            ChinhSua.visibility = View.GONE
            btn_caiDat.visibility = View.GONE
            btn_back.visibility = View.VISIBLE
        }
    }
}