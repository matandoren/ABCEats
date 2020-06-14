package com.example.android2finalproject.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.example.android2finalproject.R
import com.example.android2finalproject.model.Inspection
import com.example.android2finalproject.model.Violation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_inspection_details.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InspectionDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InspectionDetailsFragment(private val inspection: Inspection? = null) : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_inspection_details, container, false)

        // Retrieve all the violation objects from the database and then filter them for the ones that appear in this inspection
        val ref = FirebaseDatabase.getInstance().reference.child("violations")
        val allViolations = mutableListOf<Pair<String, Violation>>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                for (instance in p0.children) {
                    val key = instance.key
                    val violation = instance.getValue(Violation::class.java)
                    if (violation != null && key != null)
                        allViolations.add(Pair(key, violation))
                }
            }
        })

        val filteredViolations = allViolations.filter{pair -> inspection?.violations_keys?.contains(pair.first) ?: false}
        val layoutParams : LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        var i = 1
        for (pair in filteredViolations) {
            val textView = TextView(context)
            textView.layoutParams = layoutParams
            textView.text = "$i) ${pair.second.description}"
            view.inspection_details_linear_layout.addView(textView)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InspectionDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InspectionDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
