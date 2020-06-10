package com.example.android2finalproject.activitys

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.android2finalproject.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.sign_in_menu, menu)
        return true
    }

    fun openInspectorActivity() {
        //TODO("Send an Email address to the activity")
        val intent = Intent(this, InspectorActivity::class.java)
        startActivity(intent)
    }

    fun openManagerActivity(){
        //TODO("Send an Email address to the activity")
        val intent = Intent(this, ManagerActivity::class.java)
        startActivity(intent)
    }


}
