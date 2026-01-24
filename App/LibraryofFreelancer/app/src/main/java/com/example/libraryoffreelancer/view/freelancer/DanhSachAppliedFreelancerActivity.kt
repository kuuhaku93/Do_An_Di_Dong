package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.ApplicationAdapter
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel

class DanhSachAppliedFreelancerActivity : AppCompatActivity(), ApplicationAdapter.AvatarFreelancerClick {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danh_sach_applied)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_danhSachApplied)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val job_id=intent.getIntExtra("job_id",0)
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()

        val btn_return = findViewById<ImageButton>(R.id.btn_return_DanhSachApplied)
        btn_return.setOnClickListener {
            this.finish()
        }

        val rev_item_job=findViewById<RecyclerView>(R.id.rec_item_job)
        val freelancerViewModel= FreelancerViewModel()
        rev_item_job.layoutManager= LinearLayoutManager(this)
        rev_item_job.adapter= ApplicationAdapter(freelancerViewModel.Load_application(job_id,token),token,this)

    }

    override fun onItemClick(freelancer_id: Int) {
        val intent= Intent(this, HoSoFreelancerActivity::class.java)
        intent.putExtra("freelancer_id",freelancer_id)
        intent.putExtra("self",false)
        startActivity(intent)
    }
}