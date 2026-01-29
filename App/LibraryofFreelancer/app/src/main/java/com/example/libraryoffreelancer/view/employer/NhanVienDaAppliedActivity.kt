package com.example.libraryoffreelancer.view.employer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.CreateContactRequest
import com.example.libraryoffreelancer.view.adapter.EmployerApplicationManagerAdapter
import com.example.libraryoffreelancer.view.freelancer.HoSoFreelancerActivity
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NhanVienDaAppliedActivity : AppCompatActivity(), EmployerApplicationManagerAdapter.OnApplicantClickListener, EmployerApplicationManagerAdapter.OnAcceptClickListener {
    private var job_deadline = ""
    private var token = ""
    private val employerViewModel= EmployerViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nhan_vien_applied)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val job_id = intent.getIntExtra("job_id",0)
        job_deadline = intent.getStringExtra("job_deadline").orEmpty()
        val sharedPref = getSharedPreferences("Pref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()

        val btn_return = findViewById<ImageButton>(R.id.btn_return_nhan_vien_da_applied)
        btn_return.setOnClickListener {
            this.finish()
        }

        val rev_item_job=findViewById<RecyclerView>(R.id.rev_item_info)
        rev_item_job.layoutManager= LinearLayoutManager(this)
        rev_item_job.adapter= EmployerApplicationManagerAdapter(employerViewModel.Load_application(job_id,token),token,this, this)
    }

    override fun OnAcceptClick(application_id: Int){
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault())
        val startDate = sdf.format(calendar.time)
        val endDate = job_deadline

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Xác nhận tuyển dụng")
        builder.setMessage("Hợp đồng sẽ được tạo với thời gian:\n\n- Bắt đầu: $startDate\n- Kết thúc: $endDate (Theo hạn Job)")

        builder.setPositiveButton("Đồng ý") { dialog, _ ->
            val data = CreateContactRequest(application_id, startDate, endDate)
            employerViewModel.createContact(token, data)
            dialog.dismiss()
            recreate()
        }

        builder.setNegativeButton("Huỷ") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
    override fun OnApplicantClick(freelancer_id: Int) {
        val intent= Intent(this, HoSoFreelancerActivity::class.java)
        intent.putExtra("freelancer_id",freelancer_id)
        intent.putExtra("self",false)
        startActivity(intent)
    }
}