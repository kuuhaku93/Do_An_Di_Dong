package com.example.libraryoffreelancer.view.employer

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.CreateRatingRequest
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel
import org.w3c.dom.Text

class DanhGiaFreelancerActivity : AppCompatActivity() {
    private val employerViewModel = EmployerViewModel()
    private val freelancerViewModel = FreelancerViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_danh_gia_freelancer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val contactId = intent.getIntExtra("contact_id", 0)
        val freelancerId = intent.getIntExtra("freelancer_id", 0)

        val sharedPref = getSharedPreferences("Pref", MODE_PRIVATE)
        val token = sharedPref.getString("token", "").orEmpty()

        val portfolio = freelancerViewModel.Load_portfolio(freelancerId, token)
        val rbRating = findViewById<RatingBar>(R.id.rb_rating_danhGiaFreelancer)
        val edtComment = findViewById<TextView>(R.id.txt_danh_gia_freelancer)
        val btnDang = findViewById<Button>(R.id.btn_dang_danh_gia_freelancer)
        val imgAvatar = findViewById<ImageView>(R.id.img_anhDaiDien_DanhGiaFreelancer)
        val txtName = findViewById<TextView>(R.id.txt_tenFreelancer_DanhGiaFreelancer)
        txtName.text = portfolio.freelancer_name
        Glide.with(this)
            .load(portfolio.avatar)
            .placeholder(R.drawable.baseline_account_circle_24)
            .error(R.drawable.baseline_account_circle_24)
            .into(imgAvatar)

        btnDang.setOnClickListener {
            val ratingValue = rbRating.rating.toFloat()
            val comment = edtComment.text.toString()
            val request = CreateRatingRequest(
                contact_id = contactId,
                comment = comment,
                rating = ratingValue,
                complete = true
            )
            val response = employerViewModel.createRating(token, request)
            if(response.success){
                Toast.makeText(this, "Đánh giá thành công", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Đánh giá thất bại:${response.message}", Toast.LENGTH_SHORT).show()
            }
            this.finish()
        }
    }
}