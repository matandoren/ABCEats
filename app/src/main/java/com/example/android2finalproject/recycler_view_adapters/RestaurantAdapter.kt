package com.example.android2finalproject.recycler_view_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android2finalproject.R
import com.example.android2finalproject.model.Restaurant
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestaurantAdapter(private val restaurantList: List<Pair<String, Restaurant>>, private val listener: ItemClickListener? = null) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
       /* called by recyclerview when ite time to *create new viewholder */
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)

        return RestaurantViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = restaurantList[position]

        var gradeID : Int = R.drawable.pending_inspection
        if (currentItem.second.grade == "A") gradeID = R.drawable.score_a
        if (currentItem.second.grade == "B") gradeID = R.drawable.score_b
        if (currentItem.second.grade == "C") gradeID = R.drawable.score_c
        if (currentItem.second.grade == "D") gradeID = R.drawable.score_d

        holder.imageView.setImageResource(gradeID)
        holder.restaurantName.text = currentItem.second.name
        var tempStr = "${currentItem.second.street1} ${currentItem.second.house_number1}"
        if (currentItem.second.street2 != null) tempStr += "\n${currentItem.second.street1} ${currentItem.second.house_number1}"
        tempStr += ", ${currentItem.second.city}"
        holder.restaurantAddress.text = tempStr
    }

    override fun getItemCount() = restaurantList.size

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.card_score
        val restaurantName: TextView = itemView.card_restaurant_name
        val restaurantAddress: TextView = itemView.card_address
        init{itemView.setOnClickListener(this)}

        override fun onClick(v: View?) {
            listener?.onItemClick(restaurantList[adapterPosition])
        }
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(restaurant_clicked: Pair<String, Restaurant>)
    }
}