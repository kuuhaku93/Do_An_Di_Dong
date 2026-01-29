package com.example.libraryoffreelancer.view.employer

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.CreateJobRequest
import com.example.libraryoffreelancer.view.adapter.ItemSkillCheck
import com.example.libraryoffreelancer.view.adapter.SkillTypeAdapter
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel
import com.example.libraryoffreelancer.viewmodel.SettingsViewModel
import com.example.libraryoffreelancer.viewmodel.pickDateTime

class FormThemCongViecActivity : AppCompatActivity(), ItemSkillCheck {
    val listIDSkill=mutableListOf<Int>()
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_them_cong_viec)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Pref = getSharedPreferences("Pref", MODE_PRIVATE)
        val token = Pref.getString("token", "").orEmpty()
        val viewModel = EmployerViewModel()

        val btn_confirm = findViewById<Button>(R.id.btn_confirm)
        val btn_addSkill = findViewById<TextView>(R.id.chip_add)

        btn_addSkill.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_them_skill_them_cong_viec, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()
            val btn_ApDung = dialogView.findViewById<Button>(R.id.btn_ApDung_ThemCongViec)
            val btn_Huy = dialogView.findViewById<Button>(R.id.btn_huy_themCongViec)
            btn_ApDung.setOnClickListener {
                Log.d("mydebug", listIDSkill.toString())
                alertDialog.dismiss()
            }
            val rev_danhsachkynang=dialogView.findViewById<RecyclerView>(R.id.rev_danhSachKyNang_themCongViec)
            rev_danhsachkynang.layoutManager = LinearLayoutManager(this)
            val settingsViewModel= SettingsViewModel()
            rev_danhsachkynang.adapter = SkillTypeAdapter(settingsViewModel.Load_list_skill(token), listIDSkill, this)

            btn_Huy.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
        val btn_back = findViewById<ImageButton>(R.id.btn_back_add_job)
        val txt_jobname = findViewById<TextView>(R.id.txt_jobname)
        val txt_luong_toi_thieu = findViewById<TextView>(R.id.txt_luong_toi_thieu)
        val txt_luong_toi_da = findViewById<TextView>(R.id.txt_luong_toi_da)
        val txt_deadline = findViewById<TextView>(R.id.txt_deadline)
        var txt_enddate = findViewById<TextView>(R.id.txt_enddate)
        val txt_moTa_congViec = findViewById<TextView>(R.id.txt_moTa_congViec)
        val txt_viTri_congViec = findViewById<TextView>(R.id.txt_viTri_congViec)
        val txt_maxNV = findViewById<TextView>(R.id.txt_soLuong_Nhanvien_ToiDa)

        txt_deadline.isFocusable = false
        txt_deadline.isClickable = true
        txt_deadline.setOnClickListener {
            pickDateTime(supportFragmentManager,txt_deadline)
        }
        txt_enddate.isFocusable = false
        txt_enddate.isClickable = true
        txt_enddate.setOnClickListener {
            pickDateTime(supportFragmentManager,txt_enddate)
        }
        btn_confirm.setOnClickListener {
            val title = txt_jobname.text.toString()
            val desc = txt_moTa_congViec.text.toString()
            val salaryMin = txt_luong_toi_thieu.text.toString().toDouble()
            val salaryMax = txt_luong_toi_da.text.toString().toDouble()
            val location = txt_viTri_congViec.text.toString()
            val maxEmp = txt_maxNV.text.toString().toInt()

            val deadlineString = txt_deadline.text.toString()
            val endDateString = txt_enddate.text.toString()

            val selectedSkillIds = listIDSkill

            val requestData = CreateJobRequest(
                title = title,
                description = desc,
                salaryMin = salaryMin,
                salaryMax = salaryMax,
                location = location,
                deadline = deadlineString,
                endDate = endDateString,
                maxEmployee = maxEmp,
                requirements = selectedSkillIds
            )
            val response = viewModel.createJob(token, requestData)
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            if (response.success) {
                finish()
            }
        }

        btn_back.setOnClickListener {
            this.finish()
        }

    }
    override fun onKyNangCheck(skillID: Int) {
        if (!listIDSkill.contains(skillID)) {
            listIDSkill.add(skillID)
        }
    }

    override fun onKyNangUnCheck(skillID: Int) {
        listIDSkill.remove(skillID)
    }
}