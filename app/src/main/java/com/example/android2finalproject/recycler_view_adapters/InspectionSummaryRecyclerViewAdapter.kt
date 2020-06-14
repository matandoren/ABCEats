package com.example.android2finalproject.recycler_view_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android2finalproject.R
import com.example.android2finalproject.model.Inspection
import kotlinx.android.synthetic.main.inspection_summary_layout.view.*

class InspectionSummaryRecyclerViewAdapter(private val inspectionsList: List<Inspection>, private val listener: ItemClickListener? = null) : RecyclerView.Adapter<InspectionSummaryRecyclerViewAdapter.InspectionSummaryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspectionSummaryViewHolder {
        /* called by recyclerview when ite time to *create new viewholder */
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inspection_summary_layout, parent, false)

        return InspectionSummaryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InspectionSummaryViewHolder, position: Int) {
        val currentItem = inspectionsList[position]

        val month: String
        month = when (currentItem.inspection_month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            else -> "December"
        }

        holder.dateET.text = "$month ${currentItem.inspection_day}, ${currentItem.inspection_year}"
        holder.resultET.text = if (currentItem.points > 0) "Violations Issued" else "No Violations"
        holder.pointsET.text = currentItem.points.toString()
    }

    override fun getItemCount() = inspectionsList.size

    inner class InspectionSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val dateET: TextView = itemView.card_inspection_date
        val resultET: TextView = itemView.card_inspection_result
        val pointsET: TextView = itemView.card_inspection_points
        init{itemView.setOnClickListener(this)}

        override fun onClick(v: View?) {
            listener?.onItemClick(inspectionsList[adapterPosition])
        }
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(inspection_clicked: Inspection)
    }
}