package com.example.android2finalproject.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.android2finalproject.R
import com.example.android2finalproject.fragments.InspectorsRecyclerFragment
import com.example.android2finalproject.fragments.RestaurantRecyclerFragment
import com.example.android2finalproject.fragments.ViolationCategoryRecyclerFragment
import com.example.android2finalproject.fragments.main.MainFragment
import com.example.android2finalproject.fragments.manager.AddViolationFragment
import com.example.android2finalproject.fragments.manager.ManagerFragment
import com.example.android2finalproject.model.Restaurant
import com.example.android2finalproject.model.ViolationCategory
import com.example.android2finalproject.recycler_view_adapters.InspectorsRecyclerViewAdapter
import com.example.android2finalproject.recycler_view_adapters.RestaurantAdapter
import com.example.android2finalproject.recycler_view_adapters.ViolationCategoryRecyclerAdapter

class ManagerActivity : AppCompatActivity(), MainFragment.RestaurantRecyclerFragmentLauncher {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        val usernameEmail = intent.getStringExtra("usernameEmail")

        val manager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = manager.findFragmentById(R.id.manager_activity_fragment_container)

        if (fragment == null) {
            fragment = ManagerFragment()
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.manager_activity_fragment_container, fragment, "0").commit()
        }
    }

    fun loadInspectorsRecyclerFragment(excludeGeneralPool: Boolean = false, listener: InspectorsRecyclerViewAdapter.ItemClickListener? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.manager_activity_fragment_container, InspectorsRecyclerFragment(excludeGeneralPool, listener)).addToBackStack(null).commit()
    }

    fun loadSearchRestaurantFragment(listener: RestaurantAdapter.ItemClickListener? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.manager_activity_fragment_container, MainFragment(this, listener)).addToBackStack("main_fragment").commit()
    }

    fun loadViolationCategoryRecyclerFragment(listener: ViolationCategoryRecyclerAdapter.ItemClickListener? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.manager_activity_fragment_container, ViolationCategoryRecyclerFragment(listener)).addToBackStack("main_fragment").commit()
    }

    fun loadAddViolationFragment(pair: Pair<String, ViolationCategory>) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.manager_activity_fragment_container, AddViolationFragment(pair)).addToBackStack("violation_categories_fragment").commit()
    }

    override fun launchRestaurantRecyclerFragment(
        list: List<Pair<String, Restaurant>>,
        listener: RestaurantAdapter.ItemClickListener?
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.manager_activity_fragment_container, RestaurantRecyclerFragment(list, listener)).addToBackStack("search_fragment").commit()
    }
}
