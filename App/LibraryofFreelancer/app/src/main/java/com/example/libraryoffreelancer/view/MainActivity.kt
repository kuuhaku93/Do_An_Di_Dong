package com.example.libraryoffreelancer.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.viewmodel.AccountViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val edt_tai_khoan=findViewById<EditText>(R.id.edt_tai_khoan)
        val edt_mat_khau=findViewById<EditText>(R.id.edt_mat_khau)
        val spn_vai_tro=findViewById<Spinner>(R.id.spn_vai_tro)
        val txt_quen_mat_khau=findViewById<TextView>(R.id.txt_quen_mat_khau)
        txt_quen_mat_khau.setOnClickListener {
            val chuyen_trang= Intent(this, QuenMatKhauActivity::class.java)
            startActivity(chuyen_trang)
        }
        val btn_dang_nhap=findViewById<Button>(R.id.btn_dangNhap)
        btn_dang_nhap.setOnClickListener {
            //if(edt_tai_khoan.text.isEmpty()||edt_mat_khau.text.isEmpty()){

            //}
        }
        val txt_dang_ky=findViewById<TextView>(R.id.txt_dang_ky)
        txt_dang_ky.setOnClickListener {
            val chuyen_trang= Intent(this, DangKyActivity::class.java)
            startActivity(chuyen_trang)
        }
        /*val btn_dangNhap=findViewById<Button>(R.id.btn_dangNhap)
        btn_dangNhap.setOnClickListener {
           /* val accountViewModel= AccountViewModel()
            accountViewModel.Login("kuuhaku","1@345678")*/
            val test= Intent(this, QuenMatKhauActivity::class.java)
            startActivity(test)
        }
        val btn_dangXuat=findViewById<Button>(R.id.btn_dangXuat)
        btn_dangXuat.setOnClickListener {
            val accountViewModel= AccountViewModel()
            accountViewModel.Logout("d5a4a5d76ad5fa0cb0900fa62eb3b2bee2efab2a")
        }*/

    }
}