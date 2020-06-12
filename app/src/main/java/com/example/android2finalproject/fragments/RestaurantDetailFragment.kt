package com.example.android2finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.example.android2finalproject.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantDetailFragment : Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_restaurant_detail, container, false)

        val restaurantName = view.findViewById<EditText>(R.id.restaurant_fragment_restaurant_name_textView)
        val restaurantAddress = view.findViewById<EditText>(R.id.restaurant_fragment_address_textView)
        val inspectionDate = view.findViewById<EditText>(R.id.restaurant_fragment_inspection_date_textView)
        val result = view.findViewById<EditText>(R.id.restaurant_fragment_result_textView)
        val grade = view.findViewById<EditText>(R.id.restaurant_fragment_points_textView)

        // values that we ge from firebase DataBase
        val firebaseRestaurantName = null
        val firebaseRestaurantAddress = null
        val firebaseInspectionDate = null
        val firebaseResult = null
        val firebaseGrade = null

        // how to set to the TextView a string that comse from the firebase
        // we just need to do:
        // restaurantName.text = restaurantName.text.toString() + firebaseRestaurantName



        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RestaurantDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RestaurantDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
