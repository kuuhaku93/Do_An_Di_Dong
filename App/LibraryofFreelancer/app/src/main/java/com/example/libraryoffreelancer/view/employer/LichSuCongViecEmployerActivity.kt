package com.example.libraryoffreelancer.view.employer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.EmployerJobHistoryAdapter
import com.example.libraryoffreelancer.view.adapter.HistoryJobAdapter
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel

class LichSuCongViecEmployerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lich_su_cong_viec_employer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val employerViewModel = EmployerViewModel()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()
        Log.d("mydebug",token.toString())
        val rev_lichSu = findViewById<RecyclerView>(R.id.rev_lichSuCongViecEmployer)
        rev_lichSu.layoutManager= LinearLayoutManager(this)
        rev_lichSu.adapter= EmployerJobHistoryAdapter(employerViewModel.loadJobHistory(token))
        val btn_back = findViewById<ImageButton>(R.id.btn_QuayLai_LichSuEmployer)
        btn_back.setOnClickListener {
            finish()
        }
    }
}