package com.example.android2finalproject.fragments.main

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android2finalproject.R
import com.example.android2finalproject.model.Restaurant
import com.example.android2finalproject.recycler_view_adapters.RestaurantAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment(private val launcher: RestaurantRecyclerFragmentLauncher? = null, private val listener: RestaurantAdapter.ItemClickListener? = null) : Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)


        /*
        Both ways are equal the difference the first we find the ID
        the 2nd we can do the function thorough the id

        val restaurantName = view.findViewById<EditText>(R.id.enter_restaurant_name_fragment_main_editText)

        enter_restaurant_name_fragment_main_editText.setText("aloha")
        */
        val restaurantName = view.findViewById<EditText>(R.id.enter_restaurant_name_fragment_main_editText)
        val restaurantStreetName = view.findViewById<EditText>(R.id.enter_restaurant_street_fragment_main_editText)
        val radioGroup = view.search_restaurant_radio_group
        val searchButton= view.search_button

        searchButton.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().reference.child("restaurants")
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(context, "Failed to retrieve restaurants", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val allRestaurants = mutableListOf<Pair<String, Restaurant>>()

                    for (childSnapShot in p0.children) {
                        val key = childSnapShot.key
                        val restaurant = childSnapShot.getValue(Restaurant::class.java)
                        if (key != null && restaurant != null)
                            allRestaurants.add(Pair(key, restaurant))
                    }

                    val postNameFilterList = if (restaurantName.text.toString().isNotEmpty()) {
                        val nameCriteria = restaurantName.text.toString()
                        allRestaurants.filter { pair: Pair<String, Restaurant> ->
                            pair.second.name.toLowerCase().contains(nameCriteria.toLowerCase())
                        }
                    } else
                        allRestaurants

                    val postStreetFilterList = if (restaurantStreetName.text.toString().isNotEmpty()) {
                        val streetNameCriteria = restaurantStreetName.text.toString()
                        postNameFilterList.filter { pair: Pair<String, Restaurant> ->
                            pair.second.street1.toLowerCase().contains(streetNameCriteria.toLowerCase()) || pair.second.street2?.toLowerCase()?.contains(streetNameCriteria.toLowerCase()) ?: false
                        }
                    } else
                        postNameFilterList

                    val postGradeFilterList = if (radioGroup.checkedRadioButtonId == R.id.radio_button_a_grade)
                        postStreetFilterList.filter{pair: Pair<String, Restaurant> ->
                            pair.second.grade == "A"
                        }
                    else if (radioGroup.checkedRadioButtonId == R.id.radio_button_b_grade)
                        postStreetFilterList.filter{pair: Pair<String, Restaurant> ->
                            pair.second.grade == "B"
                        }
                    else if (radioGroup.checkedRadioButtonId == R.id.radio_button_c_grade)
                        postStreetFilterList.filter{pair: Pair<String, Restaurant> ->
                            pair.second.grade == "C"
                        }
                    else if (radioGroup.checkedRadioButtonId == R.id.radio_button_d_grade)
                        postStreetFilterList.filter{pair: Pair<String, Restaurant> ->
                            pair.second.grade == "D"
                        }
                    else
                        postStreetFilterList

                    launcher?.launchRestaurantRecyclerFragment(postGradeFilterList, listener)
                }
            })
        }

        return view
        //return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    interface RestaurantRecyclerFragmentLauncher {
        fun launchRestaurantRecyclerFragment(list: List<Pair<String, Restaurant>>, listener: RestaurantAdapter.ItemClickListener? = null)
    }
}
