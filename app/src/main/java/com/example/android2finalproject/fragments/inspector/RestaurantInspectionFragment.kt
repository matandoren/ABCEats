package com.example.android2finalproject.fragments.inspector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import com.example.android2finalproject.R
import com.example.android2finalproject.activities.InspectorActivity
import com.example.android2finalproject.model.Inspection
import com.example.android2finalproject.model.Restaurant
import com.example.android2finalproject.model.ViolationCategory
import com.example.android2finalproject.recycler_view_adapters.ViolationCategoryRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantInspectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantInspectionFragment(private val key: String?=null, private val restaurant: Restaurant?=null, private val inspectorName : String?=null) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val inspection = Inspection()

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
        val view = inflater.inflate(R.layout.fragment_restaurant_inspection, container, false)

        val addViolation = view.findViewById<Button>(R.id.fragment_restaurant_inspection_add_violation_button)
        val submit = view.findViewById<Button>(R.id.fragment_restaurant_inspection_submit_button)
        // button set on false that a person want submit with out any violation
        //submit.isEnabled = false

        addViolation.setOnClickListener {
            if(key!=null && restaurant !=null)
                addViolation(key, restaurant)
        }

        submit.setOnClickListener {submitInspection()}

        return view
    }

    private fun submitInspection() {
        val calendar = Calendar.getInstance()
        inspection.inspection_year = calendar.get(Calendar.YEAR)
        inspection.inspection_month = calendar.get(Calendar.MONTH) + 1
        inspection.inspection_day = calendar.get(Calendar.DAY_OF_MONTH)
        if (key != null) {
            inspection.restaurant_key = key
        }
        if (inspectorName != null) {
            inspection.inspector_username = inspectorName
        }

        restaurant?.grade = if(inspection.points < 14) "A"
        else if(inspection.points < 28) "B"
        else "C"

        if(restaurant?.grade == "A") {
            calendar.add(Calendar.YEAR,1)
            restaurant.next_inspection_year = calendar.get(Calendar.YEAR)
            restaurant.next_inspection_month = calendar.get(Calendar.MONTH) + 1
            restaurant.next_inspection_day = calendar.get(Calendar.DAY_OF_MONTH)
        }
        else if(restaurant?.grade == "B"){
            calendar.add(Calendar.MONTH,7)
            restaurant.next_inspection_year = calendar.get(Calendar.YEAR)
            restaurant.next_inspection_month = calendar.get(Calendar.MONTH) + 1
            restaurant.next_inspection_day = calendar.get(Calendar.DAY_OF_MONTH)
        }
        else if(restaurant?.grade == "C"){
            calendar.add(Calendar.MONTH,3)
            restaurant.next_inspection_year = calendar.get(Calendar.YEAR)
            restaurant.next_inspection_month = calendar.get(Calendar.MONTH) + 1
            restaurant.next_inspection_day = calendar.get(Calendar.DAY_OF_MONTH)
        }

        val inspectionsRef = FirebaseDatabase.getInstance().reference.child("inspections")
        val inspectionKey = inspectionsRef.push().key
        if (inspectionKey != null) {
            restaurant?.inspections_keys?.add(inspectionKey)
            inspectionsRef.child(inspectionKey).setValue(inspection)
        }
        if(key!=null)
            FirebaseDatabase.getInstance().reference.child("restaurants").child(key).setValue(restaurant)

        Toast.makeText(context, "Inspection submitted successfully", Toast.LENGTH_LONG).show()
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun addViolation(key: String, restaurant: Restaurant) {
        val inspectorActivity = activity as InspectorActivity?
        inspectorActivity!!.loadViolationCategoryRecyclerFragment(object : ViolationCategoryRecyclerAdapter.ItemClickListener {
            override fun onItemClick(violation_category_clicked: Pair<String, ViolationCategory>) {
                inspectorActivity.loadChooseViolationFragment(violation_category_clicked.second, inspection)
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
         * @return A new instance of fragment RestaurantInspectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RestaurantInspectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
