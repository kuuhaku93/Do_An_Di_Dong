package com.example.libraryoffreelancer.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.HistoryJob
import com.example.libraryoffreelancer.model.Item
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
