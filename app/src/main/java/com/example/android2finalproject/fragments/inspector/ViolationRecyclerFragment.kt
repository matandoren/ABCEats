package com.example.android2finalproject.fragments.inspector

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android2finalproject.R
import com.example.android2finalproject.activities.InspectorActivity
import com.example.android2finalproject.model.Inspection
import com.example.android2finalproject.model.Violation
import com.example.android2finalproject.model.ViolationCategory
import com.example.android2finalproject.recycler_view_adapters.ViolationRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_violation_category_recycler.*
import kotlinx.android.synthetic.main.fragment_violation_category_recycler.view.*
import kotlinx.android.synthetic.main.violation_condition_layout.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViolationRecyclerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViolationRecyclerFragment(private val violation_category : ViolationCategory?= null, private val inspection : Inspection?=null) : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_violation_category_recycler, container, false)
        view.fragment_violation_category_recycler_col_title.text = "Choose violation"
        view.fragment_violation_category_recycler_view.layoutManager = LinearLayoutManager(context)
        view.fragment_violation_category_recycler_view.setHasFixedSize(true)

        // Retrieve all the violation objects from the database and then filter them for the ones that appear in this category
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

                val filteredViolations = allViolations.filter{pair -> violation_category?.violations_keys?.contains(pair.first) ?: false}
                view.fragment_violation_category_recycler_view.adapter = ViolationRecyclerAdapter(filteredViolations, object :ViolationRecyclerAdapter.ItemClickListener{
                    override fun onItemClick(violation_clicked: Pair<String, Violation>) {
                        val builder = AlertDialog.Builder(context!!)
                        val view2 = layoutInflater.inflate(R.layout.violation_condition_layout, null)
                        view2.radio_button_1.isEnabled = (violation_clicked.second.least_severe_condition<=1 && violation_clicked.second.least_severe_condition+violation_clicked.second.num_of_conditions-1 >= 1)
                        view2.radio_button_2.isEnabled = (violation_clicked.second.least_severe_condition<=2 && violation_clicked.second.least_severe_condition+violation_clicked.second.num_of_conditions-1 >= 2)
                        view2.radio_button_3.isEnabled = (violation_clicked.second.least_severe_condition<=3 && violation_clicked.second.least_severe_condition+violation_clicked.second.num_of_conditions-1 >= 3)
                        view2.radio_button_4.isEnabled = (violation_clicked.second.least_severe_condition<=4 && violation_clicked.second.least_severe_condition+violation_clicked.second.num_of_conditions-1 >= 4)
                        view2.radio_button_5.isEnabled = (violation_clicked.second.least_severe_condition<=5 && violation_clicked.second.least_severe_condition+violation_clicked.second.num_of_conditions-1 >= 5)
                        when(violation_clicked.second.least_severe_condition){
                            1-> view2.radio_button_1.isChecked = true
                            2-> view2.radio_button_2.isChecked = true
                            3-> view2.radio_button_3.isChecked = true
                            4-> view2.radio_button_4.isChecked = true
                            5-> view2.radio_button_5.isChecked = true
                        }
                        builder.setTitle("Choose Points Penalty")
                        builder.setView(view2)
                        builder.setPositiveButton("Ok"){ _: DialogInterface, _: Int->
                            val rg = view2.radio_group
                            val id = rg.checkedRadioButtonId
                            val rb = view2.findViewById<RadioButton>(id)
                            val condition = rb.text.toString().toInt()

                            inspection?.violations_keys?.add(violation_clicked.first)
                            inspection?.points =
                                inspection?.points?.plus((violation_clicked.second.conditions_points[condition - violation_clicked.second.least_severe_condition]))!!
                            inspection.violations_conditions.add(condition)

                            activity?.supportFragmentManager?.popBackStack("inspector_fragment", POP_BACK_STACK_INCLUSIVE)

                        }
                        builder.setNegativeButton("Cancel"){_: DialogInterface, _: Int->}.show()
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
         * @return A new instance of fragment ViolationRecyclerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViolationRecyclerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
