package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.ItemKyNangClick
import com.example.libraryoffreelancer.view.adapter.ListTypeAdapter
import com.example.libraryoffreelancer.view.adapter.OnItemClickListener
import com.example.libraryoffreelancer.view.adapter.TrangChuFreelancerAdapter
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel
import com.example.libraryoffreelancer.viewmodel.SettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class TrangChuFreeLancerActivity : AppCompatActivity(), OnItemClickListener, ItemKyNangClick {
    val listIDSkill=mutableListOf<Int>()
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trang_chu_freelancer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()
        val userID=sharedPref.getInt("ACCOUNT_ID",0)


        val rev_CongViecFreelancer = findViewById<RecyclerView>(R.id.rcv_CongViecFreelancer)
        rev_CongViecFreelancer.layoutManager = LinearLayoutManager(this)
        val freelancerViewModel= FreelancerViewModel()
        var listJobAdapter = TrangChuFreelancerAdapter(freelancerViewModel.Load_list_job(token),this)
        rev_CongViecFreelancer.adapter= listJobAdapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_home) {
                return@setOnItemSelectedListener true
            }
            when (item.itemId) {
                R.id.nav_applied_job -> {
                    startActivity(Intent(applicationContext, CongViecDaNhanActivity::class.java))
                    overrideActivityTransition(
                        OVERRIDE_TRANSITION_OPEN,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left
                    )
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(applicationContext, HoSoFreelancerActivity::class.java).putExtra("self",true).putExtra("freelancer_id",userID))
                    overrideActivityTransition(
                        OVERRIDE_TRANSITION_OPEN,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left
                    )
                    finish()
                    true
                }
                else -> false
            }
        }
        val btn_loc_trangchu = findViewById<ImageButton>(R.id.btn_loc_trangchu)
        btn_loc_trangchu.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_loctimkiem, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()

            val btn_ApDung = dialogView.findViewById<Button>(R.id.btn_ApDung)
            val btn_Huy = dialogView.findViewById<Button>(R.id.btn_huy)
            btn_ApDung.setOnClickListener {
                Log.d("mydebug", listIDSkill.toString())
                alertDialog.dismiss()
            }
            val rev_boLocTimKiem=dialogView.findViewById<RecyclerView>(R.id.rev_boLocTimKiem_trangChu_Freelancer)
            rev_boLocTimKiem.layoutManager= LinearLayoutManager(this)
            val settingsViewModel= SettingsViewModel()
            rev_boLocTimKiem.adapter= ListTypeAdapter(settingsViewModel.Load_list_skill(token),listIDSkill, this)

            btn_Huy.setOnClickListener { alertDialog.dismiss() }

            alertDialog.show()
        }
        val txt_timkiem_trangchu=findViewById<EditText>(R.id.txt_timkiem_trangchu)
        val btn_tim=findViewById<ImageButton>(R.id.btn_tim_trangChu_Freelancer)
        btn_tim.setOnClickListener {
            if(txt_timkiem_trangchu.text.toString()==""&&listIDSkill.isEmpty()){
                listJobAdapter= TrangChuFreelancerAdapter(freelancerViewModel.Load_list_job(token),this)
            }
            else{
                listJobAdapter= TrangChuFreelancerAdapter(freelancerViewModel.Load_list_job_by_search(token,txt_timkiem_trangchu.text.toString(),listIDSkill),this)
            }
            rev_CongViecFreelancer.adapter= listJobAdapter
            rev_CongViecFreelancer.adapter?.notifyDataSetChanged()
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ChiTietCongViecFreelancerActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
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