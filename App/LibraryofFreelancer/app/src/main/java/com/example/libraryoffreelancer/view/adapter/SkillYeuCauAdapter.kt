package com.example.libraryoffreelancer.view.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.Skill
import com.example.libraryoffreelancer.model.TypeSkill

class SkillTypeAdapter(private val items: List<TypeSkill>,private val listIDSkill: MutableList<Int>, private val listenner: ItemSkillCheck): RecyclerView.Adapter<SkillTypeAdapter.SkillTypeViewHolder>() {
    class SkillTypeViewHolder(item: View): RecyclerView.ViewHolder(item){
        val txt_title = item.findViewById<TextView>(R.id.txt_title_itemSkillYeuCau)
        val rev_itemKyNangYeuCau = item.findViewById<RecyclerView>(R.id.rev_itemKyNangYeuCau)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SkillTypeAdapter.SkillTypeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skill_yeu_cau, parent, false)
        return SkillTypeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SkillTypeAdapter.SkillTypeViewHolder,
        position: Int
    ) {
        val type=items[position]
        holder.txt_title.text=type.type
        holder.rev_itemKyNangYeuCau.layoutManager= LinearLayoutManager(holder.itemView.context)
        holder.rev_itemKyNangYeuCau.adapter= ItemKyNangYeuCauAdapter(type.skills, listIDSkill, listenner)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
class ItemKyNangYeuCauAdapter(private val items: List<Skill>,private val listIDSkill: MutableList<Int>,private val listenner: ItemSkillCheck): RecyclerView.Adapter<ItemKyNangYeuCauAdapter.ItemKyNangYeuCauViewHolder>(){
    class ItemKyNangYeuCauViewHolder(item: View):RecyclerView.ViewHolder(item){
        val txt_item_ky_nang=itemView.findViewById<TextView>(R.id.txt_item_ky_nang)
        val cb_itemKyNang=itemView.findViewById<CheckBox>(R.id.cb_itemKyNang)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemKyNangYeuCauAdapter.ItemKyNangYeuCauViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ky_nang,parent,false)
        return ItemKyNangYeuCauViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemKyNangYeuCauViewHolder,
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
        }    }

    override fun getItemCount(): Int {
        return items.size
    }
}
interface ItemSkillCheck{
    fun onKyNangCheck(skillID: Int)
    fun onKyNangUnCheck(skillID: Int)
}