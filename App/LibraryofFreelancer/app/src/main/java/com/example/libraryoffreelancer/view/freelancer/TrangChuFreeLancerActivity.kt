package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.employer.ChiTietCongViecActivity
import com.example.libraryoffreelancer.view.adapter.OnItemClickListener
import com.example.libraryoffreelancer.view.adapter.TrangChuFreelancerAdapter
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class TrangChuFreeLancerActivity : AppCompatActivity(), OnItemClickListener {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trang_chu_freelancer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()

        val btn_loc_trangchu = findViewById<ImageButton>(R.id.btn_loc_trangchu)
        btn_loc_trangchu.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_loctimkiem, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()

//            val rev_KyNangTrangHoSo = dialogView.findViewById<RecyclerView>(R.id.rcv_KyNangTrangHoSo)
//            val rev_LoaiHinh = dialogView.findViewById<RecyclerView>(R.id.rcv_LoaiHinh)
//            val rev_NgonNgu = dialogView.findViewById<RecyclerView>(R.id.rcv_NgonNgu)
//            rev_KyNangTrangHoSo.layoutManager = LinearLayoutManager(this)


            val btnApDung = dialogView.findViewById<Button>(R.id.btn_ApDung)
            val btnHuy = dialogView.findViewById<Button>(R.id.btn_huy)

            btnApDung.setOnClickListener {

            }

            btnHuy.setOnClickListener { alertDialog.dismiss() }

            alertDialog.show()
        }
        val rev_CongViecFreelancer = findViewById<RecyclerView>(R.id.rcv_CongViecFreelancer)
        rev_CongViecFreelancer.layoutManager = LinearLayoutManager(this)
        val freelancerViewModel= FreelancerViewModel()
        rev_CongViecFreelancer.adapter= TrangChuFreelancerAdapter(freelancerViewModel.Load_list_job(token),this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_home) {
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
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, HoSoFreelancerActivity::class.java))
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

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ChiTietCongViecFreelancerActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}