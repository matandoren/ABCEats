package com.example.android2finalproject.fragments.inspector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.android2finalproject.R
import com.example.android2finalproject.model.Restaurant

import com.example.android2finalproject.recycler_view_adapters.RestaurantAdapter
import com.example.android2finalproject.recycler_view_adapters.RestaurantCardItem
import kotlinx.android.synthetic.main.fragment_restaurant_recycler.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InspectorGeneralPoolFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InspectorGeneralPoolFragment(private val restaurantList: List<Pair<String, Restaurant>> = listOf(), private val listener: RestaurantAdapter.ItemClickListener? = null) : Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_restaurant_recycler, container, false)



        //


        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragment_restaurant_recycler_view.adapter = RestaurantAdapter(restaurantList, listener)
        fragment_restaurant_recycler_view.layoutManager = LinearLayoutManager(activity)
        fragment_restaurant_recycler_view.setHasFixedSize(true)
    }

    // this method below just generate "random" restaurant with address and score to put in the recyclerview
    private fun generateList(): List<RestaurantCardItem> {
        val list = ArrayList<RestaurantCardItem>()
        // need to download the data from firebase
        // make for loop to put item correctly
        for (i in 0 until 20) {
            val restaurantName = "General Pool"
            val restaurantAddress = i.toString()
            val item = RestaurantCardItem(R.drawable.score_a,restaurantName, restaurantAddress)
            list += item
        }

        return list
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InspectorGeneralPoolFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InspectorGeneralPoolFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
