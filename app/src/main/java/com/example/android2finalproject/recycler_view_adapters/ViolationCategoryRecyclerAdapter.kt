package com.example.android2finalproject.recycler_view_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android2finalproject.R
import com.example.android2finalproject.model.ViolationCategory
import kotlinx.android.synthetic.main.inspectors_recycler_row_layout.view.*


class ViolationCategoryRecyclerAdapter(private val violation_category_list : List<Pair<String, ViolationCategory>>, private val listener: ItemClickListener? = null) : RecyclerView.Adapter<ViolationCategoryRecyclerAdapter.ViolationCategoryRecyclerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViolationCategoryRecyclerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inspectors_recycler_row_layout, parent, false)
        return ViolationCategoryRecyclerHolder(itemView)
    }

    override fun getItemCount() = violation_category_list.size

    override fun onBindViewHolder(holder: ViolationCategoryRecyclerHolder, position: Int) {
        val currentItem = violation_category_list[position]
        holder.violationCategoryTextView.text = currentItem.second.name
    }

    inner class ViolationCategoryRecyclerHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val violationCategoryTextView: TextView = itemView.inspector_username_TV
        init { itemView.setOnClickListener(this) }
        override fun onClick(v: View?) {
            listener?.onItemClick(violation_category_list[adapterPosition])
        }
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(violation_category_clicked: Pair<String, ViolationCategory>)
    }
}