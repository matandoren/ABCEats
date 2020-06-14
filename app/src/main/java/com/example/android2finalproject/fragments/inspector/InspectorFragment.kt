package com.example.android2finalproject.fragments.inspector

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager

import com.example.android2finalproject.R
import com.example.android2finalproject.activities.InspectorActivity
import com.example.android2finalproject.model.Restaurant
import com.example.android2finalproject.recycler_view_adapters.RestaurantAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InspectorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InspectorFragment(private val userName: String?=null) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_inspector, container, false)

        val openMyRestaurants = view.findViewById<Button>(R.id.fragment_inspector_open_my_restaurants)
        val openGeneralPool = view.findViewById<Button>(R.id.fragment_inspector_open_general_pool)

        openMyRestaurants.setOnClickListener { openMyRestaurantsRecycler() }
        openGeneralPool.setOnClickListener { openGeneralPoolRecycler() }


        return view
    }

    private fun openMyRestaurantsRecycler() {
        val queryMyRestaurant = FirebaseDatabase.getInstance().reference.child("restaurants").orderByChild("assigned_inspector_username").equalTo(userName)
        val restaurantList = mutableListOf<Pair<String,Restaurant>>()

        queryMyRestaurant.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {Toast.makeText(context, "Failed to retrieve restaurants", Toast.LENGTH_LONG).show()}
            override fun onDataChange(p0: DataSnapshot) {
                for(child in p0.children){
                    val restaurant = child.getValue(Restaurant::class.java)
                    val key = child.key
                    if(key!=null && restaurant!=null)
                        restaurantList.add(Pair(key,restaurant))
                }

                val inspectorActivity = activity as InspectorActivity?
                inspectorActivity!!.loadRestaurantRecyclerFragment(restaurantList, object:
                    RestaurantAdapter.ItemClickListener{
                    override fun onItemClick(restaurant_clicked: Pair<String, Restaurant>) {
                        val key = restaurant_clicked.first
                        val restaurant = restaurant_clicked.second
                        AlertDialog.Builder(this@InspectorFragment.context!!)
                            .setTitle("Conduct Inspection")
                            .setMessage("Are you sure you want to conduct inspection to: ${restaurant.name}")
                            .setPositiveButton("Yes"){ _: DialogInterface, _: Int->
                                val inspectorActivity = activity as InspectorActivity?
                                inspectorActivity!!.loadRestaurantInspection(key, restaurant)
                                //FirebaseDatabase.getInstance().reference.child("restaurants").child(key).setValue(restaurant)
                            }
                            .setNegativeButton("No"){_: DialogInterface, _: Int->}.show()
                    }
                })
            }


        })
    }

    private fun openGeneralPoolRecycler() {
        val queryGeneralPool = FirebaseDatabase.getInstance().reference.child("restaurants").orderByChild("assigned_inspector_username").equalTo("general_pool")
        val restaurantList = mutableListOf<Pair<String, Restaurant>>()
        queryGeneralPool.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Failed to retrieve restaurants", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (child in p0.children) {
                    print(child.getValue(Restaurant::class.java))
                    val restaurant = child.getValue(Restaurant::class.java)
                    val key = child.key
                    if (key != null && restaurant != null)
                        restaurantList.add(Pair(key, restaurant))
                }
                val inspectorActivity = activity as InspectorActivity?
                inspectorActivity!!.loadRestaurantRecyclerFragment(restaurantList, object:
                    RestaurantAdapter.ItemClickListener{
                    override fun onItemClick(restaurant_clicked: Pair<String, Restaurant>) {
                        val key = restaurant_clicked.first
                        val restaurant = restaurant_clicked.second
                        AlertDialog.Builder(this@InspectorFragment.context!!)
                            .setTitle("Confirm assignment")
                            .setMessage("Are you sure you want to assume responsibility over the restaurant: ${restaurant.name}")
                            .setPositiveButton("Yes"){ _: DialogInterface, _: Int->
                                if (userName != null) {
                                    restaurant.assigned_inspector_username = userName
                                }
                                FirebaseDatabase.getInstance().reference.child("restaurants").child(key).setValue(restaurant)
                                Toast.makeText(context, "the restaurant was assigned to you successfully", Toast.LENGTH_SHORT).show()
                                activity?.supportFragmentManager?.popBackStack()
                            }
                            .setNegativeButton("No"){ _: DialogInterface, _: Int->}.show()
                    }
                })
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InspectorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InspectorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
