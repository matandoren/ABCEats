package com.example.android2finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.android2finalproject.R
import com.example.android2finalproject.model.UsernameToRole
import com.example.android2finalproject.recycler_view_adapters.InspectorsRecyclerViewAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_inspectors_recycler.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InspectorsRecyclerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InspectorsRecyclerFragment(private val excludeGeneralPool : Boolean = false, private val listener: InspectorsRecyclerViewAdapter.ItemClickListener? = null) : Fragment() {
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
        val view =  inflater.inflate(R.layout.fragment_inspectors_recycler, container, false)
        view.fragment_inspector_recycler_view.layoutManager = LinearLayoutManager(context)
        view.fragment_inspector_recycler_view.setHasFixedSize(true)

        // Query the database for all the inspectors usernames
        val query = FirebaseDatabase.getInstance().getReference("users/").orderByChild("role").equalTo("inspector")
        val queryListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = ArrayList<String>()
                if (!excludeGeneralPool)
                    list.add("general_pool")
                // Get UsernameToRole objects and retrieve usernames
                for (childSnapShot in dataSnapshot.children) {
                    val pair = childSnapShot.getValue(UsernameToRole::class.java)
                    if (pair!!.username != "general_pool")
                        list.add(pair.username)
                }
                view.fragment_inspector_recycler_view.adapter = InspectorsRecyclerViewAdapter(list, listener)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Failed to retrieve the users list", Toast.LENGTH_SHORT).show()
            }
        }
        query.addListenerForSingleValueEvent(queryListener)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InspectorsRecyclerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InspectorsRecyclerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}