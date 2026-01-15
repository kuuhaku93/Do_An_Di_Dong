package com.example.libraryoffreelancer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.Job
import com.example.libraryoffreelancer.viewmodel.dateconvert

class TrangChuFreelancerAdapter(private val listJob: List<Job>): RecyclerView.Adapter<TrangChuFreelancerAdapter.TrangChuFreelancerViewHolder>() {
    class TrangChuFreelancerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageView=itemView.findViewById<ImageView>(R.id.img_anhDaiDien__trangChuFreelancer_item)
        val txt_title=itemView.findViewById<TextView>(R.id.txt_title_trangChuFreelancer_item)
        val txt_noiDung=itemView.findViewById<TextView>(R.id.txt_noiDung_trangChuFreelancer_item)
        val txt_mucLuong=itemView.findViewById<TextView>(R.id.txt_mucLuong_trangChuFreelancer_item)
        val txt_thoiGian=itemView.findViewById<TextView>(R.id.txt_thoiGianCongViec_trangChuFreelancer_item)
        val rev_skill=itemView.findViewById<RecyclerView>(R.id.rev_trangChuFreelancer)

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
        holder.txt_title.text=job.title
        holder.txt_noiDung.text=job.employer_name
        holder.txt_mucLuong.text=job.salalry_min.toString()+" - "+job.salalry_max
        holder.txt_thoiGian.text=dateconvert(job.publish_date)
        holder.rev_skill.layoutManager= GridLayoutManager(holder.itemView.context, 3)
        holder.rev_skill.adapter=TrangChuFreelancerItemAdapter(job.requirements)
    }

    override fun getItemCount(): Int {
        return listJob.size
    }
}

class TrangChuFreelancerItemAdapter(private val listSkill: List<String>): RecyclerView.Adapter<TrangChuFreelancerItemAdapter.TrangChuFreelancerItemViewHolder>() {
    class TrangChuFreelancerItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_kyNang=itemView.findViewById<TextView>(R.id.txt_kyNang_kyNangTrangChuFreelancer_item)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrangChuFreelancerItemViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_trang_chu_freelancer,parent,false)
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