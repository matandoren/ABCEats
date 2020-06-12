package com.example.android2finalproject.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.android2finalproject.R
import com.example.android2finalproject.activities.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase

class InspectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspector)


        val usernameEmail = intent.getStringExtra("usernameEmail")

        val queryGeneralPool = FirebaseDatabase.getInstance().reference.child("restaurants").orderByChild("assigned_inspector_username").equalTo("general_pool")
        val queryMyRestaurant= FirebaseDatabase.getInstance().reference.child("restaurants/").orderByChild("assigned_inspector_username").equalTo(usernameEmail)


        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }

}