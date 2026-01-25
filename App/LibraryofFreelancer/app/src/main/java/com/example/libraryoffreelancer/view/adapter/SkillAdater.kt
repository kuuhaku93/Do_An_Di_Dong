package com.example.libraryoffreelancer.view.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.Skill
import com.example.libraryoffreelancer.model.TypeSkill
import com.example.libraryoffreelancer.viewmodel.dateconvert

class ListTypeAdapter(private val items: List<TypeSkill>,private val listIDSkill: MutableList<Int>,private val listenner: ItemKyNangClick): RecyclerView.Adapter<ListTypeAdapter.ListTypeViewHolder>() {
    class ListTypeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_title=itemView.findViewById<TextView>(R.id.txt_title_itemBoLocTimKiem)
        val rev_itemKyNang=itemView.findViewById<RecyclerView>(R.id.rev_itemKyNang)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListTypeViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_bo_loc_tim_kiem,parent,false)
        return ListTypeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListTypeViewHolder,
        position: Int
    ) {
        val type=items[position]
        holder.txt_title.text=type.type
        holder.rev_itemKyNang.layoutManager= LinearLayoutManager(holder.itemView.context)
        holder.rev_itemKyNang.adapter= ItemKyNangAdapter(type.skills, listIDSkill, listenner)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ItemKyNangAdapter(private val items: List<Skill>,private val listIDSkill: MutableList<Int>,private val listenner: ItemKyNangClick): RecyclerView.Adapter<ItemKyNangAdapter.ItemKyNangViewHolder>() {
    class ItemKyNangViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_item_ky_nang=itemView.findViewById<TextView>(R.id.txt_item_ky_nang)
        val cb_itemKyNang=itemView.findViewById<CheckBox>(R.id.cb_itemKyNang)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemKyNangViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_ky_nang,parent,false)
        return ItemKyNangViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemKyNangViewHolder,
        position: Int
    ) {
        holder.txt_item_ky_nang.text=items[position].skill_name
        holder.cb_itemKyNang.buttonTintList = ColorStateList.valueOf(Color.parseColor("#0C9300"))
        holder.cb_itemKyNang.setOnCheckedChangeListener(null)
        if (items[position].id in listIDSkill){
            holder.cb_itemKyNang.isChecked=true
        }
        else holder.cb_itemKyNang.isChecked=false
        holder.cb_itemKyNang.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                listenner.onKyNangCheck(items[position].id)

            }
            else{
                listenner.onKyNangUnCheck(items[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
interface ItemKyNangClick{
    fun onKyNangCheck(skillID: Int)
    fun onKyNangUnCheck(skillID: Int)
}