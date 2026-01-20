package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.HistoryJobAdapter
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel

class LichSuCongViecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lich_su_cong_viec)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val freelancerViewModel= FreelancerViewModel()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()
        val  rev_LichSu=findViewById<RecyclerView>(R.id.rev_LichSu)
        rev_LichSu.layoutManager= LinearLayoutManager(this)
        rev_LichSu.adapter= HistoryJobAdapter(freelancerViewModel.Load_history_job(token))
        val btn_back2=findViewById<ImageButton>(R.id.btn_QuayLaiTrangNutCaiDat)
        btn_back2.setOnClickListener {
            finish()
        }
    }
}