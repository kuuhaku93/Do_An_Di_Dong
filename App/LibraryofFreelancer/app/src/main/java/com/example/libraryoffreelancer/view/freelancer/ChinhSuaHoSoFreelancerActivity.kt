package com.example.libraryoffreelancer.view.freelancer

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.CustomItem
import com.example.libraryoffreelancer.model.DefaultItem
import com.example.libraryoffreelancer.model.Item
import com.example.libraryoffreelancer.model.ItemRequest
import com.example.libraryoffreelancer.view.adapter.ItemChungNhanClick
import com.example.libraryoffreelancer.view.adapter.ItemChungNhanCustomAdapter
import com.example.libraryoffreelancer.view.adapter.ItemKyNangClick
import com.example.libraryoffreelancer.view.adapter.ListTypeAdapter
import com.example.libraryoffreelancer.view.adapter.ListTypeChungNhanAdapter
import com.example.libraryoffreelancer.view.adapter.SkillSpinnerAdapter
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel
import com.example.libraryoffreelancer.viewmodel.SettingsViewModel
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ChinhSuaHoSoFreelancerActivity : AppCompatActivity(), ItemKyNangClick, ItemChungNhanClick,
    ItemChungNhanCustomAdapter.ItemChungNhanCustomClick {
    var listIDItem= mutableListOf<ItemRequest>()
    var listIDSkill=mutableListOf<Int>()

    var listCustom=mutableListOf<CustomItem>()
    var items:List<Item> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chinh_sua_ho_so_freelancer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token=sharedPref.getString("token","").orEmpty()
        val userID=sharedPref.getInt("ACCOUNT_ID",0)
        val freelancerViewModel= FreelancerViewModel()
        val portfolio=freelancerViewModel.Load_portfolio(userID,token)


        val img_avatar=findViewById<ImageView>(R.id.img_avatar_chinhSuaHoSo_freelancer)
        Glide.with(this )
            .load(portfolio.avatar)
            .into(img_avatar)
        img_avatar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_avatar_change_chinhsuahoso_freelancer, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()
            val txt_avatar=dialogView.findViewById<EditText>(R.id.txt_avatar_ChinhSuaHoSo_freelancer)
            txt_avatar.setText(portfolio.avatar)

            val btn_ApDung = dialogView.findViewById<Button>(R.id.btn_ApDung)
            val btn_Huy = dialogView.findViewById<Button>(R.id.btn_huy)
            btn_ApDung.setOnClickListener {
                portfolio.avatar=txt_avatar.text.toString()
                alertDialog.dismiss()
            }

            btn_Huy.setOnClickListener { alertDialog.dismiss() }

            alertDialog.show()
        }

        val txt_HovaTen=findViewById<EditText>(R.id.txt_HovaTen_ChinhSuaHoSo)
        txt_HovaTen.setText(portfolio.freelancer_name)
        val txt_Email=findViewById<EditText>(R.id.txt_Email_ChinhSuaHoSo)
        txt_Email.setText(portfolio.email)
        val txt_SDT=findViewById<EditText>(R.id.txt_SDT_ChinhSuaHoSo)
        txt_SDT.setText(portfolio.phone_number)
        val txt_Mota=findViewById<EditText>(R.id.txt_moTa_ChinhSuaHoSo_freelancer)
        txt_Mota.setText(portfolio.description)
        items=portfolio.items
        for (i in portfolio.skills){
            for(j in i.skills){
                listIDSkill.add(j.id)
            }
        }
        for (i in items){
            for(j in i.default){
                listIDItem.add(ItemRequest(j.id,j.start_year!!,j.end_year!!))
            }
        }
        for (type in portfolio.items) {
            for (item in type.custom) {
                listCustom.add(item)
            }
        }

        val btn_chinhSuaKyNang=findViewById<Button>(R.id.btn_chinhSuaKyNang_ChinhSuaHoSo_freelancer)
        btn_chinhSuaKyNang.setOnClickListener {
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
        val btn_chinhSuaChungNhan=findViewById<Button>(R.id.btn_chinhSuaChungNhan_ChinhSuaHoSo_freelancer3)
        btn_chinhSuaChungNhan.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_chinhsuahoso_themchungchi, null)
            builder.setView(dialogView)

            val alertDialog = builder.create()

            val btn_ApDung = dialogView.findViewById<Button>(R.id.btn_ApDung_ThemItem_freelancer)
            val btn_Huy = dialogView.findViewById<Button>(R.id.btn_huy_them_ky_nang)
            btn_ApDung.setOnClickListener {

                alertDialog.dismiss()
            }
            val rev_itemDefault=dialogView.findViewById<RecyclerView>(R.id.rev_itemDefault_chinhSuaHoSo_freelancer)
            rev_itemDefault.layoutManager= LinearLayoutManager(this)
            val settingsViewModel= SettingsViewModel()
            rev_itemDefault.adapter= ListTypeChungNhanAdapter(settingsViewModel.Load_list_item(token),listIDItem,items,this)
            val rev_itemCustom=dialogView.findViewById<RecyclerView>(R.id.rev_itemCustom_chinhSuaHoSo_freelancer)
            rev_itemCustom.layoutManager= LinearLayoutManager(this)
            val listitem=settingsViewModel.Load_list_item(token)
            val listcustom:MutableList<CustomItem> = mutableListOf()
            for (type in listitem) {
                for (item in type.custom) {
                    listcustom.add(item)
                }
            }
            rev_itemCustom.adapter= ItemChungNhanCustomAdapter(listcustom,listCustom, this)

            val btn_themItemCustom=dialogView.findViewById<Button>(R.id.btn_themItemCustom_ThemSkill)
            btn_themItemCustom.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.dialog_them_custom_item, null)
                builder.setView(dialogView)

                val alertDialog = builder.create()

                val btn_ApDung = dialogView.findViewById<Button>(R.id.btn_ApDung_ThemItemCustom_freelancer)
                val btn_Huy = dialogView.findViewById<Button>(R.id.btn_Huy_themItem_freelancer)

                val txt_tenChungNhan=dialogView.findViewById<EditText>(R.id.txt_tenChungNhan_themChungNhan_freelancer)
                val sp_loaiChungNhan=dialogView.findViewById<Spinner>(R.id.sp_loaiChungNhan_themChungNhan_freelancer)
                val txt_mota=dialogView.findViewById<EditText>(R.id.txt_mota_thmeChungNhan_freelancer)
                val items = settingsViewModel.Load_list_item(token)

                sp_loaiChungNhan.adapter = SkillSpinnerAdapter(this, items)

                btn_Huy.setOnClickListener { alertDialog.dismiss() }
                btn_ApDung.setOnClickListener {
                    val selectedType = sp_loaiChungNhan.selectedItem as Item
                    listcustom.add(CustomItem(selectedType.type_id,txt_tenChungNhan.text.toString(),txt_mota.text.toString(),selectedType.default.first().icon))
                    rev_itemCustom.adapter?.notifyDataSetChanged()
                    alertDialog.dismiss()
                }
                alertDialog.show()
            }
            btn_Huy.setOnClickListener { alertDialog.dismiss() }

            alertDialog.show()

        }

        val btn_Luu=findViewById<Button>(R.id.btn_Luu_ChinhSuaHoSo)
         btn_Luu.setOnClickListener {
//             Log.d("mydebug", portfolio.toString())
//             Log.d("mydebug", listIDSkill.toString())
             val listitem:List<ItemRequest> = listIDItem.toList()
             val listcustom:List<CustomItem> = listCustom.toList()

//             Log.d("mydebug", Json.encodeToString(ListSerializer(ItemRequest.serializer()),listitem))
//             Log.d("mydebug", Json.encodeToString(ListSerializer(CustomItem.serializer()),listcustom))
             val freelancerViewModel= FreelancerViewModel()
             val res=freelancerViewModel.Edit_Portfolio(token,txt_HovaTen.text.toString(),portfolio.avatar,txt_Email.text.toString(),txt_SDT.text.toString(),txt_Mota.text.toString(),listIDSkill,listitem,listcustom)
             if (res.success){
                 Toast.makeText(this,"Cập nhật thành công",Toast.LENGTH_SHORT).show()
                 this.finish()
             }
             else{
                 Toast.makeText(this,"Cập nhật thất bại\nLỗi: ${res.message}",Toast.LENGTH_SHORT).show()
             }
         }

        val btn_QuayLaiTrangHoSo = findViewById<ImageButton>(R.id.btn_QuayLai_chinhSuaHoSo_freelancer)
        btn_QuayLaiTrangHoSo.setOnClickListener {
            finish()
        }

    }

    override fun onKyNangCheck(skillID: Int) {
        if(!listIDSkill.contains(skillID)){
            listIDSkill.add(skillID)
        }
    }

    override fun onKyNangUnCheck(skillID: Int) {
        listIDSkill.remove(skillID)
    }

    override fun onChungNhanCheck(newList: List<DefaultItem>, typeId: Int,newIDItem: MutableList<ItemRequest>) {
        val index=items.indexOfFirst { it.type_id==typeId }
        items[index].default=newList
        listIDItem=newIDItem

        Log.d("mydebug","listIDItem: $listIDItem")
        Log.d("mydebug","newList: $newList")

    }

    override fun onChungNhanUnCheck(
        newList: List<DefaultItem>,
        typeId: Int,
        newIDItem: MutableList<ItemRequest>
    ) {
        val index=items.indexOfFirst { it.type_id==typeId }
        items[index].default=newList
        listIDItem=newIDItem
    }

    override fun onCustomCheck(item: CustomItem) {
        if (!listCustom.contains(item))
            listCustom.add(item)
    }

    override fun onCustomUnCheck(item: CustomItem) {
        listCustom.remove(item)
    }

}