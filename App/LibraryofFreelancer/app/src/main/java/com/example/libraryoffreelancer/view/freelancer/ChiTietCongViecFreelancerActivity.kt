package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.TrangChuFreelancerItemAdapter
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel
import com.example.libraryoffreelancer.viewmodel.dateconvert

class ChiTietCongViecFreelancerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chi_tiet_cong_viec_freelancer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val job_id=intent.getIntExtra("position",0)
        val freelancerViewModel= FreelancerViewModel()
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()
        val listJob=freelancerViewModel.Load_list_job(token)
        val job=listJob[job_id]

        val img_avatar_chiTietCongViec=findViewById<ImageView>(R.id.img_avatar_chiTietCongViec)
        Glide.with(this)
            .load(job.avatar)
            .into(img_avatar_chiTietCongViec)
        val txt_soNguoiDaThamGia=findViewById<TextView>(R.id.txt_SoNguoiDaThamGia)
        txt_soNguoiDaThamGia.text=job.current_employee.toString()+"/"+job.max_employee.toString()
        val txt_TenCongTy=findViewById<TextView>(R.id.txt_TenCongTy)
        txt_TenCongTy.text=job.employer_name
        val txt_TenCongViecEmployer=findViewById<TextView>(R.id.txt_TenCongViecEmployer)
        txt_TenCongViecEmployer.text=job.title
        val txt_thoigianungtuyen=findViewById<TextView>(R.id.txt_thoigianungtuyen)
        txt_thoigianungtuyen.text= dateconvert(job.publish_date)
        val txt_mucluong=findViewById<TextView>(R.id.txt_mucluong)
        txt_mucluong.text=job.salary_min.toString()+" - "+job.salary_max.toString()+" \$"
        val txt_vitri=findViewById<TextView>(R.id.txt_vitri)
        txt_vitri.text=job.location
        val txt_thoigiandeadline=findViewById<TextView>(R.id.txt_thoigiandeadline)
        txt_thoigiandeadline.text=dateconvert(job.deadline)
        val txt_motachitietcongviec=findViewById<TextView>(R.id.txt_motachitietcongviec)
        txt_motachitietcongviec.text=job.description
        val rev_kyNang_chiTietCongViec=findViewById<RecyclerView>(R.id.rev_kyNang_chiTietCongViec)
        rev_kyNang_chiTietCongViec.layoutManager= GridLayoutManager(this, 3)
        rev_kyNang_chiTietCongViec.adapter= TrangChuFreelancerItemAdapter(job.requirements)


        val btn_ungtuyencongviec = findViewById<Button>(R.id.btn_ungtuyencongviec)
        btn_ungtuyencongviec.setOnClickListener {
            val intent = Intent(this, DangKyCongViecActivity::class.java)
            intent.putExtra("job_id",job.id)
            startActivity(intent)
        }
        if(job.is_applied) {
            btn_ungtuyencongviec.isEnabled=false
            btn_ungtuyencongviec.text="Đã ứng tuyển"
        }

        val btn_commentcongviec=findViewById<ImageView>(R.id.btn_commentcongviec)
        btn_commentcongviec.setOnClickListener {
            val intent = Intent(this, DanhSachAppliedFreelancerActivity::class.java)
            intent.putExtra("job_id",job.id)
            startActivity(intent)
        }
        val btn_QuayLaiTrangChu=findViewById<ImageView>(R.id.btn_QuayLaiTrangChu)
        btn_QuayLaiTrangChu.setOnClickListener {
            this.finish()
        }
    }
}