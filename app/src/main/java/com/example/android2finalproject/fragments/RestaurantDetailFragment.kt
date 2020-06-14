package com.example.android2finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.android2finalproject.R
import com.example.android2finalproject.activities.MainActivity
import com.example.android2finalproject.model.Inspection
import com.example.android2finalproject.model.Restaurant
import com.example.android2finalproject.recycler_view_adapters.InspectionSummaryRecyclerViewAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_restaurant_detail.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantDetailFragment(private val restaurantAndKey: Pair<String, Restaurant>? = null) : Fragment() {
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
        if (restaurantAndKey == null)
            return view

        var gradeID : Int = R.drawable.pending_inspection
        if (restaurantAndKey.second.grade == "A") gradeID = R.drawable.score_a
        if (restaurantAndKey.second.grade == "B") gradeID = R.drawable.score_b
        if (restaurantAndKey.second.grade == "C") gradeID = R.drawable.score_c
        if (restaurantAndKey.second.grade == "D") gradeID = R.drawable.score_d

        view.card_score.setImageResource(gradeID)
        view.card_restaurant_name.text = restaurantAndKey.second.name
        var tempStr = "${restaurantAndKey.second.street1} ${restaurantAndKey.second.house_number1}"
        if (restaurantAndKey.second.street2 != null) tempStr += "\n${restaurantAndKey.second.street1} ${restaurantAndKey.second.house_number1}"
        tempStr += ", ${restaurantAndKey.second.city}"
        view.card_address.text = tempStr

        view.inspection_summary_recycler_view.layoutManager = LinearLayoutManager(context)
        view.inspection_summary_recycler_view.setHasFixedSize(true)

        // Query the database for all the inspections that belong to this restaurant
        val query = FirebaseDatabase.getInstance().reference.child("inspections").orderByChild("restaurant_key").equalTo(restaurantAndKey.first)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                val inspectionList = mutableListOf<Inspection>()
                for (childSnapShot in p0.children) {
                    val inspection = childSnapShot.getValue(Inspection::class.java)
                    if (inspection != null)
                        inspectionList.add(inspection)
                }

                view.inspection_summary_recycler_view.adapter = InspectionSummaryRecyclerViewAdapter(inspectionList, object: InspectionSummaryRecyclerViewAdapter.ItemClickListener {
                    override fun onItemClick(inspection_clicked: Inspection) {
                        val mainActivity = activity as MainActivity?
                        mainActivity!!.loadInspectionDetailsFragment(inspection_clicked)
                    }
                })
            }
        })

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
