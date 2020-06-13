package com.example.android2finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.android2finalproject.R
import com.example.android2finalproject.model.ViolationCategory
import com.example.android2finalproject.recycler_view_adapters.ViolationCategoryRecyclerAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_violation_category_recycler.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViolationCategoryRecyclerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViolationCategoryRecyclerFragment(private val listener: ViolationCategoryRecyclerAdapter.ItemClickListener? = null) : Fragment() {
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
        view.fragment_violation_category_recycler_view.layoutManager = LinearLayoutManager(context)
        view.fragment_violation_category_recycler_view.setHasFixedSize(true)

        // retrieve all the violation categories from the database
        val ref = FirebaseDatabase.getInstance().reference.child("violation_categories")
        val dataListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = mutableListOf<Pair<String, ViolationCategory>>()
                for (childSnapShot in dataSnapshot.children) {
                    val key = childSnapShot.key
                    val violationCategory = childSnapShot.getValue(ViolationCategory::class.java)
                    if (key != null && violationCategory != null)
                        list.add(Pair(key, violationCategory))
                }
                view.fragment_violation_category_recycler_view.adapter = ViolationCategoryRecyclerAdapter(list, listener)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Failed to retrieve the violation categories", Toast.LENGTH_SHORT).show()
            }
        }
        ref.addListenerForSingleValueEvent(dataListener)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViolationCategoryRecyclerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViolationCategoryRecyclerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
