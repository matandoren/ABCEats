package com.example.android2finalproject.fragments.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.android2finalproject.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ManagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManagerFragment : Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_manager, container, false)

        val addRestaurant = view.findViewById<Button>(R.id.add_restaurant_fragment_manager_Button)
        val addInspector = view.findViewById<Button>(R.id.add_inspector_fragment_manager_Button)
        val addViolationCategory = view.findViewById<Button>(R.id.add_violation_category_fragment_manager_Button)
        val addViolation = view.findViewById<Button>(R.id.add_violation_fragment_manager_Button)
        val assignInspector = view.findViewById<Button>(R.id.assign_inspector_fragment_manager_Button)
        val removeInspector = view.findViewById<Button>(R.id.remove_inspector_fragment_manager_Button)
        val removeRestaurant = view.findViewById<Button>(R.id.remove_restaurant_fragment_manager_Button)




        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
