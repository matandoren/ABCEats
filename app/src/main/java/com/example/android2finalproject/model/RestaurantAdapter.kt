package com.example.android2finalproject.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android2finalproject.R
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestaurantAdapter(private val exampleList: List<RestaurantCardItem>) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
       /* called by recyclerview when ite time to *create new viewholder */
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)

        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.imageView.setImageResource(currentItem.imageResource)
        holder.restaurantName.text = currentItem.restaurantName
        holder.restaurantAddress.text = currentItem.restaurantAddress
    }

    override fun getItemCount() = exampleList.size

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.card_score
        val restaurantName: TextView = itemView.card_restaurant_name
        val restaurantAddress: TextView = itemView.card_address
    }
}