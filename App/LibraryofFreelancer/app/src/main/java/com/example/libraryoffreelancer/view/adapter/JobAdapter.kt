package com.example.libraryoffreelancer.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.Application
import com.example.libraryoffreelancer.model.CurrentJob
import com.example.libraryoffreelancer.model.HistoryJob
import com.example.libraryoffreelancer.view.adapter.ApplicationAdapter.AvatarFreelancerClick
import com.example.libraryoffreelancer.viewmodel.EmployerViewModel
import com.example.libraryoffreelancer.viewmodel.FreelancerViewModel
import com.example.libraryoffreelancer.viewmodel.dateconvert

class HistoryJobAdapter(private val items: List<HistoryJob>): RecyclerView.Adapter<HistoryJobAdapter.HistoryJobViewHolder>() {
    class HistoryJobViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img_avatar=itemView.findViewById<ImageView>(R.id.img_avatar_historyJob)
        val txt_tenCongViec=itemView.findViewById<TextView>(R.id.txt_ten_cong_viec_Employer)
        val txt_tenCongTy=itemView.findViewById<TextView>(R.id.txt_tenCongTy_lichSuCongViec)
        val txt_thoiGian=itemView.findViewById<TextView>(R.id.txt_thoi_gian)
        val ctlayout_vien=itemView.findViewById<View>(R.id.ctlayout_vien_lichsuCongViec_item)
        val txt_trangThai=itemView.findViewById<TextView>(R.id.txt_trangThaiLichSu)
        val txt_dangGia=itemView.findViewById<TextView>(R.id.txt_dangGiaLichSu_Freelancer)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryJobViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_lich_su_cong_viec,parent,false)
        return HistoryJobViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: HistoryJobViewHolder,
        position: Int
    ) {
        val job=items[position]

        Glide.with(holder.itemView.context)
            .load(job.company_avatar)
            .into(holder.img_avatar)
        holder.txt_tenCongViec.text=job.job_title
        holder.txt_tenCongTy.text=job.company_name
        holder.txt_thoiGian.text=dateconvert(job.start_date)+" - "+dateconvert(job.end_date)
        holder.txt_dangGia.text=job.score.toString()
        if(job.complete){
            holder.txt_trangThai.text="Hoàn Thành"
            holder.txt_trangThai.setTextColor(Color.GREEN)
            holder.ctlayout_vien.setBackgroundColor(Color.GREEN)
        }
        else{
            holder.txt_trangThai.text="Không Hoàn Thành"
            holder.txt_trangThai.setTextColor(Color.RED)
            holder.ctlayout_vien.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
class ApplicationAdapter(private val items: List<Application>,private val token: String,private val listenner: AvatarFreelancerClick): RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder>() {
    class ApplicationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img_avatar=itemView.findViewById<ImageView>(R.id.img_avatar_ItemProvider_Freelancer)
        val txt_fullname=itemView.findViewById<TextView>(R.id.txt_fullName_itemProvider_Freelancer)
        val txt_message=itemView.findViewById<TextView>(R.id.txt_message_itemProvide_freelancer)
        val txt_thoiGian=itemView.findViewById<TextView>(R.id.txt_time_itemProvider_freelancer)
        val ctlayout_vien=itemView.findViewById<View>(R.id.ctlayout_applyJob_itemProvider_freelancer)
        val txt_dangGia=itemView.findViewById<TextView>(R.id.txt_review_itemProvider_freelancer)
        val txt_mucLuong=itemView.findViewById<TextView>(R.id.txt_wantedsalary_itemProvider_freelancer)
        val rev_kynang=itemView.findViewById<RecyclerView>(R.id.rev_skill_itemProvider_Freelancer)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApplicationViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_prodvider_freelancer,parent,false)
        return ApplicationViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ApplicationViewHolder,
        position: Int
    ) {
        val application=items[position]
        val freelancerViewModel= FreelancerViewModel()
        val freelancer=freelancerViewModel.Load_portfolio(application.freelancer_id,token)

        Glide.with(holder.itemView.context)
            .load(freelancer.avatar)
            .into(holder.img_avatar)
        holder.img_avatar.setOnClickListener {
            listenner.onItemClick(application.freelancer_id)
        }
        holder.txt_fullname.text=freelancer.freelancer_name
        holder.txt_thoiGian.text=dateconvert(application.applied_date)
        holder.txt_message.text=application.description
        holder.txt_mucLuong.setText("$"+application.wanted_salary.toString())
        holder.txt_dangGia.text=freelancer.rating.toString()
        if(application.is_applied){
            holder.ctlayout_vien.setBackgroundColor(Color.GREEN)
        }
        else{
            holder.ctlayout_vien.setBackgroundColor(Color.WHITE)
        }
        holder.rev_kynang.layoutManager= GridLayoutManager(holder.itemView.context, 3)
        holder.rev_kynang.adapter= PortfolioSkillAdapter(application.skills)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface AvatarFreelancerClick{
        fun onItemClick(freelancer_id: Int)
    }
}
class CurrentJobAdapter(private val items: List<CurrentJob>,private val token: String,private val listenner: AvatarEmployerClick): RecyclerView.Adapter<CurrentJobAdapter.CurrentJobViewHolder>() {
    class CurrentJobViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img_avatar=itemView.findViewById<ImageView>(R.id.img_avatar_itemCongViecHienTai_freelancer  )
        val txt_tenCongViec=itemView.findViewById<TextView>(R.id.txt_TenCongViec_congViecHienTai_feelancer)
        val txt_tenCongTy=itemView.findViewById<TextView>(R.id.txt_TenCongTy_congViecHienTai_freelancer)
        val txt_trangThai=itemView.findViewById<TextView>(R.id.txt_trangThai_congViecHienTai_freelancer)
        val btn_danhgia=itemView.findViewById<Button>(R.id.btn_DanhGia_congViecHienTai_freelancer)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentJobViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_cong_viec_hien_tai_freelancer,parent,false)
        return CurrentJobViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CurrentJobViewHolder,
        position: Int
    ) {
        val job=items[position]
        val employerViewModel= EmployerViewModel()

        val employer=employerViewModel.loadProfileEmployer(token,job.company_id)

        Glide.with(holder.itemView.context)
            .load(employer.company_logo)
            .into(holder.img_avatar)
        holder.img_avatar.setOnClickListener {
            listenner.onItemClick(job.company_id)
        }
//        holder.btn_danhgia.setOnClickListener {
//            listenner.onDanhGiaClick(job.contact_id)
//        }
        holder.txt_tenCongViec.text=job.job_title
        holder.txt_tenCongTy.text=employer.company_name
        if (job.is_done){
            if (job.is_rating){
                holder.txt_trangThai.text="Hoàn Thành"
                holder.btn_danhgia.visibility=View.GONE
                holder.txt_trangThai.setTextColor(Color.GREEN)
            }
            else{
                holder.txt_trangThai.text="Chờ Đánh Giá"
                holder.btn_danhgia.visibility=View.VISIBLE
                holder.txt_trangThai.setTextColor(Color.GREEN)
            }
        }
        else{
            holder.txt_trangThai.text="Đang Thực Hiện"
            holder.btn_danhgia.visibility=View.GONE
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
    interface AvatarEmployerClick{
        fun onItemClick(employer_id: Int)
        fun onDanhGiaClick(contact_id: Int)
    }
}