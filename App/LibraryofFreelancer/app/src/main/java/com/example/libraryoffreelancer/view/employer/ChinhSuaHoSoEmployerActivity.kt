package com.example.libraryoffreelancer.view.employer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.UpdateProfileRequest
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel

class ChinhSuaHoSoEmployerActivity : AppCompatActivity() {
    private val employerViewModel = EmployerViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chinh_sua_ho_so_employer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack = findViewById<ImageButton>(R.id.nut_quay_lai)
        val txt_Name = findViewById<TextView>(R.id.txt_nhaptencongty)
        val txt_Email = findViewById<TextView>(R.id.txt_nhapemailcongty)
        val txt_Website = findViewById<TextView>(R.id.txt_nhapwebsite)
        val txt_Address = findViewById<TextView>(R.id.txt_chinhsuadiachi)
        val txt_Desc = findViewById<TextView>(R.id.o_nhap_mo_ta)
        val txt_Phone = findViewById<TextView>(R.id.txt_nhapsdt)
        val btnSave = findViewById<Button>(R.id.btn_luuemployer)
        val logo=findViewById<ImageView>(R.id.image_anhdaidien_chinhSuaHoSoEmployer)


        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val token = sharedPref.getString("token", "").orEmpty()
        val userID = sharedPref.getInt("ACCOUNT_ID",0)
        val employerViewModel = EmployerViewModel()
        val profile = employerViewModel.loadProfileEmployer(token, userID)

        txt_Name.setText(profile.company_name)
        txt_Email.setText(profile.email)
        txt_Website.setText(profile.website)
        txt_Address.setText(profile.address)
        txt_Desc.setText(profile.employer_description)
        Glide.with(this )
            .load(profile.company_logo)
            .into(logo)
        logo.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_logo_change_chinhsuahoso_employer, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()
            val txt_avatar=dialogView.findViewById<EditText>(R.id.txt_logo_ChinhSuaHoSo_employer)
            txt_avatar.setText(profile.company_logo)

            val btn_ApDung = dialogView.findViewById<Button>(R.id.btn_ApDung_chinhSuaHoSo_Employer)
            val btn_Huy = dialogView.findViewById<Button>(R.id.btn_huy_chinhSuaHoSo_Employer)
            btn_ApDung.setOnClickListener {
                profile.company_logo=txt_avatar.text.toString()
                alertDialog.dismiss()
            }

            btn_Huy.setOnClickListener { alertDialog.dismiss() }

            alertDialog.show()
        }
        btnSave.setOnClickListener {
            val newName = txt_Name.text.toString().trim()
            val newLogo = profile.company_logo
            val newEmail = txt_Email.text.toString().trim()
            val newWebsite = txt_Website.text.toString().trim()
            val newAddress = txt_Address.text.toString().trim()
            val newDesc = txt_Desc.text.toString().trim()
            val phone = txt_Phone.text.toString().trim()

            val request = UpdateProfileRequest(
                company_name = newName,
                company_logo = newLogo,
                email = newEmail,
                website = newWebsite,
                address = newAddress,
                employer_description = newDesc,
                phone_number = phone

            )
            val response = employerViewModel.updateProfile(token, request)
            if (response.success) {
                Toast.makeText(this@ChinhSuaHoSoEmployerActivity, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this@ChinhSuaHoSoEmployerActivity, "Lỗi: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}