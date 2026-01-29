package com.example.libraryoffreelancer.view.employer

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        val sharedPref = getSharedPreferences("Pref", MODE_PRIVATE)
        val token = sharedPref.getString("token", "").orEmpty()

        txt_Name.text = intent.getStringExtra("company_name")
        txt_Email.text = intent.getStringExtra("email")
        txt_Website.text = intent.getStringExtra("website")
        txt_Address.text = intent.getStringExtra("address")
        txt_Desc.text = intent.getStringExtra("employer_description")

        btnSave.setOnClickListener {
            val newName = txt_Name.text.toString().trim()
            val newEmail = txt_Email.text.toString().trim()
            val newWebsite = txt_Website.text.toString().trim()
            val newAddress = txt_Address.text.toString().trim()
            val newDesc = txt_Desc.text.toString().trim()
            val phone = txt_Phone.text.toString().trim()

            val request = UpdateProfileRequest(
                company_name = newName,
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