package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel

class DanhGiaEmloyerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_danh_gia_emloyer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()

        val contact_id=intent.getIntExtra("contact_id",0)
        val job_title=intent.getStringExtra("job_title").orEmpty()
        val company_name=intent.getStringExtra("company_name").orEmpty()
        val txt_tenCongViec=findViewById<TextView>(R.id.txt_tenCongViec_danhGiaEmployer_Freelancer)
        val txt_tenCongTy=findViewById<TextView>(R.id.txt_tenCongTy_danhGiaEmployer_Freelancer)
        txt_tenCongTy.text=company_name
        txt_tenCongViec.text=job_title
        val rb_rating=findViewById<RatingBar>(R.id.rb_rating_danhGiaEmployer_Freelancer)
        val txt_danh_gia=findViewById<TextView>(R.id.txt_danhGia_DanhGiaEmployer_Freelancer)
        val btn_dang_danh_gia=findViewById<Button>(R.id.btn_danhGia_danhGiaEmployer_Freelancer)
        val btn_back=findViewById<ImageButton>(R.id.btn_back_dangGiaEmployer_freelancer)
        btn_back.setOnClickListener {
            this.finish()
        }
        btn_dang_danh_gia.setOnClickListener {
            val rating=rb_rating.rating
            val comment=txt_danh_gia.text.toString()
            val freelancerViewModel= FreelancerViewModel()
            val resp=freelancerViewModel.Create_Review(contact_id,comment,rating.toDouble(),token)
            if(resp.success){
                Toast.makeText(this, "Đánh giá thành công", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Đánh giá thất bại:${resp.message}", Toast.LENGTH_SHORT).show()
            }
            this.finish()
        }
    }
}