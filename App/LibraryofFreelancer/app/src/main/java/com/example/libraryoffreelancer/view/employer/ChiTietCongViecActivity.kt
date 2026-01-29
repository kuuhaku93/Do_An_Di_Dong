package com.example.libraryoffreelancer.view.employer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.TrangChuEmployerAdapter
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel
import com.example.libraryoffreelancer.viewmodel.dateconvert

class ChiTietCongViecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chi_tiet_cong_viec_employer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id = intent.getIntExtra("position",0)
        val employerViewModel = EmployerViewModel()
        val Pref = getSharedPreferences("Pref", Context.MODE_PRIVATE)
        val token = Pref.getString("token","").orEmpty()
        val list = employerViewModel.loadTrangChuEmployer(token)
        val job = list[id]

        val img_avatar = findViewById<ImageView>(R.id.img_avatar_chiTietCongViecEmployer)
        Glide.with(this)
            .load(job.avatar)
            .into(img_avatar)
        val txt_nguoiThamGia = findViewById<TextView>(R.id.txt_SoNguoiDaThamGia)
        val txt_tenCongTy = findViewById<TextView>(R.id.txt_TenCongTyEmployer)
        val txt_tenCongViec = findViewById<TextView>(R.id.txt_TenCongViec)
        val txt_deadline = findViewById<TextView>(R.id.txt_thoiGianDeadline)
        val rev_kyNang_chiTietCongViec=findViewById<RecyclerView>(R.id.rev_kyNang_chiTiet)
        val txt_mucLuong = findViewById<TextView>(R.id.txt_mucluongemploy)
        val txt_viTri = findViewById<TextView>(R.id.txt_vitriemploy)
        val txt_thoiGianUngTuyen = findViewById<TextView>(R.id.txt_thoigianungtuyenemploy)
        val txt_moTa = findViewById<TextView>(R.id.txt_motachitietcongviec)

        txt_nguoiThamGia.text = job.current_employee.toString()+"/"+job.max_employee.toString()
        txt_tenCongTy.text=job.employer_name
        txt_tenCongViec.text=job.title
        txt_deadline.text=dateconvert(job.deadline)
        txt_mucLuong.text=job.salary_min.toString()+" - "+job.salary_max.toString()+" \$"
        txt_viTri.text=job.location
        txt_thoiGianUngTuyen.text= dateconvert(job.publish_date)
        txt_moTa.text = job.description

        rev_kyNang_chiTietCongViec.layoutManager= GridLayoutManager(this, 3)
        rev_kyNang_chiTietCongViec.adapter= TrangChuEmployerAdapter.TrangChuEmployerKyNangAdapter(job.requirements)

        val btn_QuayLaiTrangChuEmployer = findViewById<ImageButton>(R.id.btn_QuayLaiTrangChuEmployer)
        btn_QuayLaiTrangChuEmployer.setOnClickListener {
            this.finish()
        }

        val btn_off = findViewById<ImageButton>(R.id.btn_off)
        btn_off.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder
                .setMessage("Bạn có muốn kết thúc tuyển dụng?")
                .setTitle("Thông báo")
                .setPositiveButton("Đồng ý") { dialog, which ->
                    val jobId = job.id
                    val result = employerViewModel.closeJob(token,jobId)
                    if (result.success){
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Huỷ") { dialog, which ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        val btn_comment = findViewById<ImageButton>(R.id.btn_commentcongviecEmployer)
        btn_comment.setOnClickListener {
            val intent = Intent(this, NhanVienDaAppliedActivity::class.java)
            intent.putExtra("job_id", job.id)
            intent.putExtra("job_deadline", job.deadline)
            startActivity(intent)
        }
    }
}