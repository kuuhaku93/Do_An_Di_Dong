package com.example.libraryoffreelancer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.Item
import com.example.libraryoffreelancer.model.Rating

class PortfolioAdapter(private val items: List<Item>): RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {
    class PortfolioViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_title=itemView.findViewById<TextView>(R.id.txt_hocvan)
        val rev_item=itemView.findViewById<RecyclerView>(R.id.rcv_hocvan)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortfolioViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_hocvan_hoso,parent,false)
        return PortfolioViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PortfolioViewHolder,
        position: Int
    ) {
        holder.txt_title.text=items[position].type
        holder.rev_item.layoutManager= LinearLayoutManager(holder.itemView.context)
        holder.rev_item.adapter=PortfolioItemAdapter(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class PortfolioItemAdapter(private val item: Item): RecyclerView.Adapter<PortfolioItemAdapter.PortfolioItemViewHolder>() {
    class PortfolioItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img_anh=itemView.findViewById<ImageView>(R.id.img_anh_portfolio_item)
        val txt_title=itemView.findViewById<TextView>(R.id.txt_tieude_portfolio_item)
        val txt_mota=itemView.findViewById<TextView>(R.id.txt_mota_portfolio_item)
        val txt_namBatDau=itemView.findViewById<TextView>(R.id.txt_namBatDau_portfolio_item)
        val txt_namKetThuc=itemView.findViewById<TextView>(R.id.txt_namKetThuc_portfolio_item)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortfolioItemViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_portfolio,parent,false)
        return PortfolioItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PortfolioItemViewHolder,
        position: Int
    ) {
        if(position<item.default.size){
            holder.txt_title.text=item.default[position].title
            holder.txt_mota.text=item.default[position].description
            holder.txt_namBatDau.text=item.default[position].start_year.toString()
            holder.txt_namKetThuc.text=item.default[position].end_year.toString()
            Glide.with(holder.itemView.context)
                .load(item.default[position].icon)
                .into(holder.img_anh)
        }
        else {
            holder.txt_title.text=item.custom[position-item.default.size].title
            holder.txt_mota.text=item.custom[position-item.default.size].description
            Glide.with(holder.itemView.context)
                .load(item.custom[position-item.default.size].icon)
                .into(holder.img_anh)
        }
    }

    override fun getItemCount(): Int {
        return item.default.size+item.custom.size
    }
}

class PortfolioSkillAdapter(private val listSkill: List<String>): RecyclerView.Adapter<PortfolioSkillAdapter.PortfolioSkillViewHolder>() {
    class PortfolioSkillViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_kyNang=itemView.findViewById<TextView>(R.id.txt_item_kynang_congviec)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PortfolioSkillViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_icon_kynang_congviec,parent,false)
        return PortfolioSkillViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PortfolioSkillViewHolder,
        position: Int
    ) {
        holder.txt_kyNang.text=listSkill[position]
    }

    override fun getItemCount(): Int {
        return listSkill.size
    }
}

class RatingAdapter(private val items: List<Rating>): RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {
    class RatingViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txt_tenReviewer=itemView.findViewById<TextView>(R.id.txt_tenReviewer)
        val txt_ngayDang=itemView.findViewById<TextView>(R.id.txt_ThoiGianDaDangReview)
        val txt_comment=itemView.findViewById<TextView>(R.id.txt_commentReview)
        val img_avatar=itemView.findViewById<ImageView>(R.id.image_avatar_review)
        val txt_sao=itemView.findViewById<TextView>(R.id.so_sao_danh_gia)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatingViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_review,parent,false)
        return RatingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RatingViewHolder,
        position: Int
    ) {
        holder.txt_tenReviewer.text=items[position].employer_name
        holder.txt_ngayDang.text=items[position].created_at
        holder.txt_comment.text=items[position].comment
        holder.txt_sao.text=items[position].rating.toString()
        Glide.with(holder.itemView.context)
            .load(items[position].employer_avatar)
            .into(holder.img_avatar)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}