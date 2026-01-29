package com.example.libraryoffreelancer.view.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.CustomItem
import com.example.libraryoffreelancer.model.DefaultItem
import com.example.libraryoffreelancer.model.Item
import com.example.libraryoffreelancer.model.ItemRequest
import com.example.libraryoffreelancer.model.Skill
import com.example.libraryoffreelancer.model.TypeSkill

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

class ListTypeChungNhanAdapter(private val items: List<Item>, private val listIDItem: MutableList<ItemRequest>, private val current: List<Item>, private val listenner: ItemChungNhanClick): RecyclerView.Adapter<ListTypeChungNhanAdapter.ListTypeChungNhanViewHolder>() {
    class ListTypeChungNhanViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_title_chungChi_freelancer=itemView.findViewById<TextView>(R.id.txt_title_chungChi_freelancer)
        val rev_chungChi=itemView.findViewById<RecyclerView>(R.id.rev_chungChi)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListTypeChungNhanViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_loai_chung_chi,parent,false)
        return ListTypeChungNhanViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListTypeChungNhanViewHolder,
        position: Int
    ) {
        val type=items[position]
        val currentType=current.find { it.type == type.type }
        val curentList: MutableList<DefaultItem> = mutableListOf()
        holder.txt_title_chungChi_freelancer.text=type.type
        for (item in currentType?.default!!) {
            curentList.add(item)
        }
        holder.rev_chungChi.layoutManager= LinearLayoutManager(holder.itemView.context)
        Log.d("mydebug",listIDItem.toString())
        holder.rev_chungChi.adapter= ItemChungNhanAdapter(type.default, listIDItem,type.type_id,curentList, listenner)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ItemChungNhanAdapter(private val items: List<DefaultItem>, private val listIDItem: MutableList<ItemRequest>, private val type_id: Int, private val currentList: MutableList<DefaultItem>, private val listenner: ItemChungNhanClick): RecyclerView.Adapter<ItemChungNhanAdapter.ItemChungNhanViewHolder>() {
    class ItemChungNhanViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val cb_item=itemView.findViewById<CheckBox>(R.id.cb_item)
        val img_icon_chungChi_freelancer=itemView.findViewById<ImageView>(R.id.img_icon_chungChi_freelancer)
        val txt_startYear=itemView.findViewById<TextView>(R.id.txt_startYear)
        val txt_endYear=itemView.findViewById<TextView>(R.id.txt_endYear)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemChungNhanViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_chung_nhan,parent,false)
        return ItemChungNhanViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemChungNhanViewHolder,
        position: Int
    ) {
        Log.d("mydebug","listIDItem: ${items[position]}")
        val item=items[position]
        Log.d("mydebug","item: ${item}")
        Glide.with(holder.itemView.context)
            .load(item.icon)
            .error(R.drawable.error)
            .into(holder.img_icon_chungChi_freelancer)
        holder.cb_item.setText(item.title)
        holder.cb_item.buttonTintList = ColorStateList.valueOf(Color.parseColor("#0C9300"))
        holder.cb_item.setOnCheckedChangeListener(null)

        if (listIDItem.any { it.id==item.id}){
            holder.cb_item.isChecked=true
            var index=listIDItem.indexOfFirst { it.id==item.id }
            if(index==-1){
                currentList.add(DefaultItem(item.id,item.title,"",item.icon,start_year = 2000,end_year = 2000))
                listIDItem.add(ItemRequest(item.id,2000,2000))
                index=listIDItem.indexOfFirst { it.id==item.id }
            }
            val currentItem = currentList.find { it.id == item.id }!!
            holder.txt_startYear.text=currentItem.start_year.toString()
            holder.txt_endYear.text=currentItem.end_year.toString()
            holder.txt_startYear.visibility=View.VISIBLE
            holder.txt_endYear.visibility=View.VISIBLE

        }
        else {
            holder.cb_item.isChecked=false
            holder.txt_startYear.visibility=View.GONE
            holder.txt_endYear.visibility=View.GONE
        }
        holder.txt_startYear.setOnClickListener {
            val numberPicker = NumberPicker(holder.itemView.context)
            numberPicker.minValue = 1980
            numberPicker.maxValue = 2026
            numberPicker.value = 2026

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Chọn năm")
                .setView(numberPicker)
                .setPositiveButton("OK") { _, _ ->
                    holder.txt_startYear.text= numberPicker.value.toString()
                    var index=listIDItem.indexOfFirst { it.id==item.id }
                    if(index==-1){
                        currentList.add(DefaultItem(item.id,item.title,"",item.icon,start_year = 2000,end_year = 2000))
                        listIDItem.add(ItemRequest(item.id,2000,2000))
                        index=listIDItem.indexOfFirst { it.id==item.id }
                    }
                    val currentItem = currentList.find { it.id == item.id }
                    val idItem = listIDItem.find { it.id == item.id }

                    if (currentItem != null && idItem != null) {
                        currentItem.start_year = numberPicker.value
                        idItem.start_year = numberPicker.value
                    }

                    Log.d("mydebug","Năm được chọn: ${numberPicker.value}")
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
        holder.txt_endYear.setOnClickListener {
            val numberPicker = NumberPicker(holder.itemView.context)
            numberPicker.minValue = 1980
            numberPicker.maxValue = 2026
            numberPicker.value = 2026

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Chọn năm")
                .setView(numberPicker)
                .setPositiveButton("OK") { _, _ ->
                    holder.txt_endYear.text= numberPicker.value.toString()
                    var index=listIDItem.indexOfFirst { it.id==item.id }
                    if(index==-1){
                        currentList.add(DefaultItem(item.id,item.title,"",item.icon,start_year = 2000,end_year = 2000))
                        listIDItem.add(ItemRequest(item.id,2000,2000))
                    }
                    val currentItem = currentList.find { it.id == item.id }
                    val idItem = listIDItem.find { it.id == item.id }

                    if (currentItem != null && idItem != null) {
                        currentItem.end_year = numberPicker.value
                        idItem.end_year = numberPicker.value
                    }
                    Log.d("mydebug","Năm được chọn: ${numberPicker.value}")
                }
                .setNegativeButton("Hủy", null)
                .show()

        }
        holder.cb_item.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                var index=listIDItem.indexOfFirst { it.id==item.id }
                if(index==-1) {
                    currentList.add(DefaultItem(item.id,item.title,"",item.icon,start_year = 2000,end_year = 2000))
                    listIDItem.add(ItemRequest(item.id,2000,2000))
                    index=listIDItem.indexOfFirst { it.id==item.id }
                }
                holder.txt_startYear.visibility=View.VISIBLE
                holder.txt_endYear.visibility=View.VISIBLE
                holder.txt_startYear.text=listIDItem[index].start_year.toString()
                holder.txt_endYear.text=listIDItem[index].end_year.toString()

//                val startYear=holder.txt_startYear.text.toString().toInt()
//                val endYear=holder.txt_endYear.text.toString().toInt()
                listenner.onChungNhanCheck(currentList, type_id,listIDItem)
            }
            else{
                currentList.remove(items[position])
                listIDItem.remove(listIDItem.find { it.id==item.id })
                holder.txt_startYear.visibility=View.GONE
                holder.txt_endYear.visibility=View.GONE

                listenner.onChungNhanUnCheck(currentList, type_id,listIDItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
interface ItemChungNhanClick{
    fun onChungNhanCheck(newList:List<DefaultItem>,typeId: Int,newIDItem: MutableList<ItemRequest>)
    fun onChungNhanUnCheck(newList:List<DefaultItem>,typeId: Int,newIDItem: MutableList<ItemRequest>)

}
class ItemChungNhanCustomAdapter(private val items: MutableList<CustomItem>, private val listCustom: MutableList<CustomItem>, private val listenner: ItemChungNhanCustomClick): RecyclerView.Adapter<ItemChungNhanCustomAdapter.ItemChungNhanCustomViewHolder>() {
    class ItemChungNhanCustomViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img_icon=itemView.findViewById<ImageView>(R.id.img_icon_chungChiCustom_freelancer)
        val cb_itemCustom=itemView.findViewById<CheckBox>(R.id.cb_itemCustom)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemChungNhanCustomViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_chung_chi_custom,parent,false)
        return ItemChungNhanCustomViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemChungNhanCustomViewHolder,
        position: Int
    ) {
        val item=items[position]
        Glide.with(holder.itemView.context)
            .load(item.icon)
            .error(R.drawable.error)
            .into(holder.img_icon)
        holder.cb_itemCustom.text=item.title
        holder.cb_itemCustom.buttonTintList = ColorStateList.valueOf(Color.parseColor("#0C9300"))
        holder.cb_itemCustom.setOnCheckedChangeListener(null)
        if (listCustom.contains(item)){
            holder.cb_itemCustom.isChecked=true
        }
        else holder.cb_itemCustom.isChecked=false
        holder.cb_itemCustom.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                listenner.onCustomCheck(item)

            }
            else{
                listenner.onCustomUnCheck(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    interface ItemChungNhanCustomClick{
        fun onCustomCheck(item:CustomItem)
        fun onCustomUnCheck(item: CustomItem)
    }
}

class SkillSpinnerAdapter(context: Context, private val items: List<Item>) : ArrayAdapter<Item>(
    context,
    R.layout.item_loai_item,
    items
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.text = items[position].type
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.text = items[position].type
        return view
    }
}