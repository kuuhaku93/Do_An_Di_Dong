package com.example.libraryoffreelancer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryoffreelancer.R
import com.example.libraryoffreelancer.model.EmployerReview

class EmployerReviewAdapter(private var reviews: List<EmployerReview>) : RecyclerView.Adapter<EmployerReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.txt_title_item)
        val tvScore: TextView = view.findViewById(R.id.txt_soSao_item)
        val tvComment: TextView = view.findViewById(R.id.txt_noiDung_item)
        val tvDate: TextView = view.findViewById(R.id.txt_ngayDanhGia_item)
        val imgAvatar: ImageView = view.findViewById(R.id.img_anhDaiDien_NguoiDanhGia_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_danhgia_employer, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvName.text = review.freelancer_name
        holder.tvScore.text = review.score.toString()
        holder.tvComment.text = review.comment

        holder.tvDate.text = review.created_at.take(10)
        Glide.with(holder.itemView.context)
            .load(review.freelancer_avatar)
            .placeholder(R.drawable.about)
            .into(holder.imgAvatar)
    }

    override fun getItemCount() = reviews.size
}