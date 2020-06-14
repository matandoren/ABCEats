package com.example.android2finalproject.recycler_view_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android2finalproject.R
import com.example.android2finalproject.model.Violation
import kotlinx.android.synthetic.main.inspectors_recycler_row_layout.view.*

class ViolationRecyclerAdapter(private val violation_list : List<Pair<String, Violation>>, private val listener: ItemClickListener? = null) : RecyclerView.Adapter<ViolationRecyclerAdapter.ViolationRecyclerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViolationRecyclerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inspectors_recycler_row_layout, parent, false)
        return ViolationRecyclerHolder(itemView)
    }

    override fun getItemCount() = violation_list.size

    override fun onBindViewHolder(holder: ViolationRecyclerHolder, position: Int) {
        val currentItem = violation_list[position]
        holder.violationTextView.text = currentItem.second.description
    }

    inner class ViolationRecyclerHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val violationTextView: TextView = itemView.inspector_username_TV
        init { itemView.setOnClickListener(this) }
        override fun onClick(v: View?) {
            listener?.onItemClick(violation_list[adapterPosition])
        }
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(violation_clicked: Pair<String, Violation>)
    }
}