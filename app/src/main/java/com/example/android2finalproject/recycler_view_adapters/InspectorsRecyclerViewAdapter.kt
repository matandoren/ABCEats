package com.example.android2finalproject.recycler_view_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android2finalproject.R
import kotlinx.android.synthetic.main.inspectors_recycler_row_layout.view.*

class InspectorsRecyclerViewAdapter(private val inspectors_username_list : List<String>, private val listener: ItemClickListener? = null) : RecyclerView.Adapter<InspectorsRecyclerViewAdapter.InspectorsRecyclerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InspectorsRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inspectors_recycler_row_layout, parent, false)
        return InspectorsRecyclerViewHolder(itemView)
    }

    override fun getItemCount() = inspectors_username_list.size

    override fun onBindViewHolder(holder: InspectorsRecyclerViewHolder, position: Int) {
        val currentItem = inspectors_username_list[position]
        holder.usernameTextView.text = currentItem
    }

    inner class InspectorsRecyclerViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val usernameTextView: TextView = itemView.inspector_username_TV
        init { itemView.setOnClickListener(this) }
        override fun onClick(v: View?) {
            listener?.onItemClick(inspectors_username_list[adapterPosition])
        }
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(username_clicked: String)
    }
}