package com.example.android2finalproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.android2finalproject.R
import com.example.android2finalproject.fragments.RestaurantRecyclerFragment
import com.example.android2finalproject.fragments.ViolationCategoryRecyclerFragment
import com.example.android2finalproject.fragments.inspector.InspectorFragment
import com.example.android2finalproject.fragments.inspector.RestaurantInspectionFragment
import com.example.android2finalproject.model.Restaurant
import com.example.android2finalproject.recycler_view_adapters.RestaurantAdapter
import com.example.android2finalproject.recycler_view_adapters.ViolationCategoryRecyclerAdapter

class InspectorActivity : AppCompatActivity() {
    var myEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspector)

        val manager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = manager.findFragmentById(R.id.manager_activity_fragment_container)

        val usernameEmail = intent.getStringExtra("usernameEmail")
        myEmail = usernameEmail

        if (fragment == null) {
            fragment = InspectorFragment(myEmail)
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.inspector_activity_fragment_container, fragment, "0").commit()
        }


    }

    fun loadRestaurantRecyclerFragment(restaurantList: List<Pair<String, Restaurant>>,listener: RestaurantAdapter.ItemClickListener){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.inspector_activity_fragment_container, RestaurantRecyclerFragment(restaurantList, listener)).addToBackStack(null).commit()
    }

    fun loadRestaurantInspection(key: String, restaurant: Restaurant) {
        val fragment = RestaurantInspectionFragment(key, restaurant)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.inspector_activity_fragment_container,fragment,"1").addToBackStack(null).commit()

    }


    fun loadViolationCategoryRecyclerFragment(listener: ViolationCategoryRecyclerAdapter.ItemClickListener? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.inspector_activity_fragment_container, ViolationCategoryRecyclerFragment(listener)).addToBackStack("inspector_fragment").commit()
    }

    fun loadChooseViolationFragment(listener: ViolationCategoryRecyclerAdapter.ItemClickListener? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.manager_activity_fragment_container, ViolationCategoryRecyclerFragment(listener)).addToBackStack("violation_categories_fragment").commit()
    }
}
