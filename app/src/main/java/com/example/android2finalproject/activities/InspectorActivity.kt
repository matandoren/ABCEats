package com.example.android2finalproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.android2finalproject.R
import com.example.android2finalproject.fragments.inspector.InspectorFragment

class InspectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspector)


        val manager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = manager.findFragmentById(R.id.main_activity_fragment_container)

        if (fragment == null) {
            fragment = InspectorFragment()
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.inspector_activity_fragment_container, fragment, "0").commit()
        }
    }
}
