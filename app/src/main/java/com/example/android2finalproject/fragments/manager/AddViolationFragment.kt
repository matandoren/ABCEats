package com.example.android2finalproject.fragments.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager

import com.example.android2finalproject.R
import com.example.android2finalproject.activities.ManagerActivity
import com.example.android2finalproject.model.Violation
import com.example.android2finalproject.model.ViolationCategory
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_violation.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddViolationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddViolationFragment(private val key_and_violation_category: Pair<String, ViolationCategory>? = null) : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_add_violation, container, false)

        view.add_violation_submit_button.setOnClickListener {
            if (view.add_violation_description_editText.text.toString().isEmpty())
                Toast.makeText(context, "The description must not be empty", Toast.LENGTH_LONG).show()
            else {
                val pointsArray = intArrayOf(if (view.add_violation_c1_ET.text.toString().isEmpty()) 0 else view.add_violation_c1_ET.text.toString().toInt(),
                    if (view.add_violation_c2_ET.text.toString().isEmpty()) 0 else view.add_violation_c2_ET.text.toString().toInt(),
                    if (view.add_violation_c3_ET.text.toString().isEmpty()) 0 else view.add_violation_c3_ET.text.toString().toInt(),
                    if (view.add_violation_c4_ET.text.toString().isEmpty()) 0 else view.add_violation_c4_ET.text.toString().toInt(),
                    if (view.add_violation_c5_ET.text.toString().isEmpty()) 0 else view.add_violation_c5_ET.text.toString().toInt())

                val result = evaluatePointsArray(pointsArray)
                if (result.first == -1)
                    Toast.makeText(context, "Non-blank/zero point penalties must be consecutive and there must be at least one", Toast.LENGTH_LONG).show()
                else { // if we've gotten this far, it means that all the input is valid
                    val mutableList = mutableListOf<Int>()
                    for (i in pointsArray)
                        if (i > 0)
                            mutableList.add(i)
                    val violation = Violation(view.add_violation_description_editText.text.toString(),
                        result.first + 1,
                        result.second,
                        mutableList)

                    // insert the violation to the database and update the appropriate violation category in the database
                    val ref = FirebaseDatabase.getInstance().reference
                    val key = ref.child("violations").push().key
                    if (key != null) {
                        ref.child("violations").child(key).setValue(violation)
                        val violationCategory = key_and_violation_category?.second
                        val violationCategoryKey = key_and_violation_category?.first
                        if (violationCategory != null && violationCategoryKey != null) {
                            violationCategory.violations_keys.add(key)
                            ref.child("violation_categories")
                                .child(violationCategoryKey)
                                .setValue(violationCategory)

                            Toast.makeText(context, "The violation was added successfully", Toast.LENGTH_LONG).show()
                            val managerActivity = activity as ManagerActivity?
                            activity?.supportFragmentManager?.popBackStack("main_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                    }
                }
            }
        }

        return view
    }

    private fun evaluatePointsArray(pointsArray: IntArray) : Pair<Int, Int> { // returns -1 in the first value of the pair if the points array is invalid
        var firstNonZeroIdx = 0
        var lastNonZeroIdx = 4
        while (firstNonZeroIdx < lastNonZeroIdx && pointsArray[firstNonZeroIdx] == 0) firstNonZeroIdx++
        while (lastNonZeroIdx > firstNonZeroIdx && pointsArray[lastNonZeroIdx] == 0) lastNonZeroIdx--
        val size = lastNonZeroIdx - firstNonZeroIdx + 1
        if (pointsArray[firstNonZeroIdx] == 0) return Pair(-1, size) // there are no values greater than zero in the array
        while (lastNonZeroIdx > firstNonZeroIdx)
            if (pointsArray[lastNonZeroIdx] == 0)
                return Pair(-1, size) // there non-zero values are not consecutive
            else
                lastNonZeroIdx--
        return Pair(firstNonZeroIdx, size)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddViolationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddViolationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
