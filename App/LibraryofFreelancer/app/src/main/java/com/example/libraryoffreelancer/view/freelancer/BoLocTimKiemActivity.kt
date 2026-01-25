package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.ItemKyNangClick
import com.example.libraryoffreelancer.view.adapter.ListTypeAdapter
import com.example.libraryoffreelancer.viewmodel.SettingsViewModel

class BoLocTimKiemActivity : AppCompatActivity(), ItemKyNangClick {
    val listIDSkill= mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bo_loc_tim_kiem)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()

        val btn_back=findViewById<ImageButton>(R.id.btn_back_boLocTimKiem_Freelancer)
        btn_back.setOnClickListener {
            this.finish()
        }
        val btn_ApDung=findViewById<Button>(R.id.btn_ApDung_boLocTimKiem_freelancer)
        btn_ApDung.setOnClickListener {
            Log.d("mydebug", listIDSkill.toString())
            this.finish()
        }
        val btn_huy=findViewById<Button>(R.id.btn_huy_boLocTimKiem_Freelancer)
        btn_huy.setOnClickListener {
            this.finish()
        }
        val rev_boLocTimKiem=findViewById<RecyclerView>(R.id.rev_boLocTimKiem)
        rev_boLocTimKiem.layoutManager= LinearLayoutManager(this)
        val settingsViewModel= SettingsViewModel()
        rev_boLocTimKiem.adapter= ListTypeAdapter(settingsViewModel.Load_list_skill(token), listIDSkill,this)

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