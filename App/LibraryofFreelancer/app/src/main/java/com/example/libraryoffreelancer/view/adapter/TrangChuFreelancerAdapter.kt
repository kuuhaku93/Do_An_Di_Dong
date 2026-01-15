package com.example.libraryoffreelancer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.Job
import com.example.libraryoffreelancer.viewmodel.dateconvert

class TrangChuFreelancerAdapter(private val listJob: List<Job>, private val listener: OnItemClickListener): RecyclerView.Adapter<TrangChuFreelancerAdapter.TrangChuFreelancerViewHolder>() {
    class TrangChuFreelancerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img_avatar=itemView.findViewById<ImageView>(R.id.img_anhDaiDien__trangChuFreelancer_item)
        val txt_title=itemView.findViewById<TextView>(R.id.txt_title_trangChuFreelancer_item)
        val txt_noiDung=itemView.findViewById<TextView>(R.id.txt_noiDung_trangChuFreelancer_item)
        val txt_mucLuong=itemView.findViewById<TextView>(R.id.txt_mucLuong_trangChuFreelancer_item)
        val txt_thoiGian=itemView.findViewById<TextView>(R.id.txt_thoiGianCongViec_trangChuFreelancer_item)
        val rev_skill=itemView.findViewById<RecyclerView>(R.id.rev_trangChuFreelancer)
        val  txt_soLuong=itemView.findViewById<TextView>(R.id.txt_soLuong_kyNagnTrangChuFreelanxer_item)
        val layout_item=itemView.findViewById<View>(R.id.layout_itemFreelancer)


    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrangChuFreelancerViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_trang_chu_freelancer,parent,false)
        return TrangChuFreelancerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrangChuFreelancerViewHolder,
        position: Int
    ) {
        val job=listJob[position]

        Glide.with(holder.itemView.context)
            .load(job.avatar)
            .into(holder.img_avatar)

        holder.txt_title.text=job.title
        holder.txt_noiDung.text=job.employer_name
        holder.txt_mucLuong.text=job.salary_min.toString()+" - "+job.salary_max
        holder.txt_thoiGian.text=dateconvert(job.publish_date)
        holder.rev_skill.layoutManager= GridLayoutManager(holder.itemView.context, 3)
        holder.rev_skill.adapter=TrangChuFreelancerItemAdapter(job.requirements)
        holder.txt_soLuong.text=job.current_employee.toString()+"/"+job.max_employee.toString()
        holder.layout_item.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return listJob.size
    }
}
interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class TrangChuFreelancerItemAdapter(private val listSkill: List<String>): RecyclerView.Adapter<TrangChuFreelancerItemAdapter.TrangChuFreelancerItemViewHolder>() {
    class TrangChuFreelancerItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_kyNang=itemView.findViewById<TextView>(R.id.txt_item_kynang_congviec)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrangChuFreelancerItemViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_icon_kynang_congviec,parent,false)
        return TrangChuFreelancerItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrangChuFreelancerItemViewHolder,
        position: Int
    ) {
        holder.txt_kyNang.text=listSkill[position]
    }

    override fun getItemCount(): Int {
        return listSkill.size
    }
}