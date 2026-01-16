package com.example.libraryoffreelancer.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryoffreelancer.model.EmployerJob

class EmployerJobsAdapter(private val list: List<EmployerJob>, private val listener: OnPostClickListener): RecyclerView.Adapter<EmployerJobsAdapter.EmployerViewHolder>() {
    class EmployerViewHolder(item: View): RecyclerView.ViewHolder(item){
        //val layout:
    }
    interface OnPostClickListener {
        fun OnPostClick(position: Int)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployerViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: EmployerViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}