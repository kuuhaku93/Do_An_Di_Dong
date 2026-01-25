package com.example.libraryoffreelancer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.model.EmployerJob
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.view.adapter.TrangChuFreelancerItemAdapter.TrangChuFreelancerItemViewHolder
import com.example.libraryoffreelancer.viewmodel.dateconvert


class TrangChuEmployerAdapter (private var listCV: List<EmployerJob>, private val listener: OnJobClickListener): RecyclerView.Adapter<TrangChuEmployerAdapter.EmployerViewHolder>(){
    class EmployerViewHolder(item: View): RecyclerView.ViewHolder(item){
        val layout = item.findViewById<View>(R.id.layout_itemEmployer)
        val anhDaiDien = item.findViewById<ImageView>(R.id.img_anhDaiDien_trangChuEmployer_item)
        val tieuDe=itemView.findViewById<TextView>(R.id.txt_title_trangChuEmployer_item)
        val noiDung=itemView.findViewById<TextView>(R.id.txt_noiDung_trangChuEmployer_item)
        val mucLuong=itemView.findViewById<TextView>(R.id.txt_mucLuong_trangChuEmployer_item)
        val thoiGian=itemView.findViewById<TextView>(R.id.txt_thoiGianCongViec_trangChuEmployer_item)
        val rev_kyNang=itemView.findViewById<RecyclerView>(R.id.rev_kyNangYeuCau)
        val soLuong=itemView.findViewById<TextView>(R.id.txt_soLuongNhanVien_item)
    }
    interface OnJobClickListener {
        //fun onJobClick(position: Int)
        fun onJobClick(job: EmployerJob)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrangChuEmployerAdapter.EmployerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trang_chu_employer, parent, false)
        return EmployerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrangChuEmployerAdapter.EmployerViewHolder,
        position: Int
    ) {
        val cv = listCV[position]
        Glide.with(holder.itemView.context)
            .load(cv.avatar)
            .into(holder.anhDaiDien)

        holder.tieuDe.text = cv.title
        holder.noiDung.text = cv.description
        holder.mucLuong.text = cv.salary_min.toString() + " - " + cv.salary_max.toString()+ " \$"
        holder.thoiGian.text = dateconvert(cv.publish_date)
        holder.soLuong.text=cv.current_employee.toString()+"/"+cv.max_employee.toString()
        holder.rev_kyNang.layoutManager = GridLayoutManager(holder.itemView.context, 3)
        holder.rev_kyNang.adapter = TrangChuEmployerKyNangAdapter(cv.requirements)
        holder.layout.setOnClickListener {
            //listener.onJobClick(position)
            listener.onJobClick(cv)
        }
    }
    fun updateData(newList: List<EmployerJob>) {
        this.listCV = newList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return listCV.size
    }
    class TrangChuEmployerKyNangAdapter(private val listKN: List<String>): RecyclerView.Adapter<TrangChuEmployerKyNangAdapter.SkillsViewHolder>(){
        class SkillsViewHolder(item: View): RecyclerView.ViewHolder(item){
            val kyNang=itemView.findViewById<TextView>(R.id.txt_item_kynang_congviec)
        }
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): TrangChuEmployerKyNangAdapter.SkillsViewHolder {
            val view= LayoutInflater.from(parent.context).inflate(R.layout.item_icon_kynang_congviec,parent,false)
            return SkillsViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: TrangChuEmployerKyNangAdapter.SkillsViewHolder,
            position: Int
        ) {
           holder.kyNang.text = listKN[position]
        }

        override fun getItemCount(): Int {
            return listKN.size
        }
    }
}