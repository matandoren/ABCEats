package com.example.android2finalproject.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.android2finalproject.R
import com.example.android2finalproject.fragments.RestaurantDetailFragment
import com.example.android2finalproject.fragments.RestaurantRecyclerFragment
import com.example.android2finalproject.fragments.main.InspectionDetailsFragment
import com.example.android2finalproject.fragments.main.MainFragment
import com.example.android2finalproject.model.Inspection
import com.example.android2finalproject.model.Restaurant
import com.example.android2finalproject.model.UsernameToRole
import com.example.android2finalproject.recycler_view_adapters.RestaurantAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(), MainFragment.RestaurantRecyclerFragmentLauncher {
    private val mAuth = FirebaseAuth.getInstance()
    private val mDatabase = FirebaseDatabase.getInstance().reference
    //private val email ="timakudev@gmail.com"
    //private val password ="123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = manager.findFragmentById(R.id.main_activity_fragment_container)

        if (fragment == null) {
            fragment = MainFragment(this, object: RestaurantAdapter.ItemClickListener {
                override fun onItemClick(restaurant_clicked: Pair<String, Restaurant>) {
                    loadRestaurantDetailFragment(restaurant_clicked)
                }
            })
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.main_activity_fragment_container, fragment, "0").commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.sign_in_menu, menu)
        return true
    }

    fun openInspectorActivity(username: String) {
        val intent = Intent(this, InspectorActivity::class.java)
        intent.putExtra("usernameEmail", username)
        startActivity(intent)
    }

    fun openManagerActivity(username: String) {
        val intent = Intent(this, ManagerActivity::class.java)
        intent.putExtra("usernameEmail", username)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.login_icon_1){
            //Toast.makeText(this, "Sign in selected", Toast.LENGTH_LONG).show()
            launchSignInPopUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchSignInPopUp() {

        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.username_and_password_layout, null)
        builder.setTitle("Sign in")
        builder.setView(view)
        builder.setPositiveButton("Ok") { _: DialogInterface, _: Int ->
            val usernameET = view.findViewById<EditText>(R.id.sign_in_username_ET)
            val passwordET = view.findViewById<EditText>(R.id.sign_in_password_ET)
            /* checks email and password if empty */
            if (usernameET.text.toString().isEmpty() || passwordET.text.toString().isEmpty())
                Toast.makeText(
                    this,
                    "The username and the password must not be empty",
                    Toast.LENGTH_LONG
                ).show()
            else
                mAuth.signInWithEmailAndPassword(usernameET.text.toString(), passwordET.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        // Sign in success, query the database for user's role
                        val username = usernameET.text.toString()
                        val query = mDatabase.child("users").orderByChild("username").equalTo(username)
                        val queryListener = object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Get UsernameToRole object and find what is the role of this user
                                for (childSnapShot in dataSnapshot.children) { // it is assumed that there will be only one child
                                    val pair = childSnapShot.getValue(UsernameToRole::class.java)
                                    if (pair?.role.equals("manager")) {
                                        if (pair != null) {
                                            openManagerActivity(pair.username)
                                        }
                                        return
                                    }
                                    if (pair?.role.equals("inspector")) {
                                        if (pair != null) {
                                            openInspectorActivity(pair.username)
                                        }
                                        return
                                    }
                                }
                                Toast.makeText(this@MainActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Toast.makeText(this@MainActivity, "Failed to retrieve the UserToRole object for this user", Toast.LENGTH_SHORT).show()
                            }
                        }
                        query.addListenerForSingleValueEvent(queryListener)

                        /*val username = usernameET.text.toString()
                        val pair = UsernameToRole("general_pool", "inspector")
                        val key = mDatabase.child("users").push().key
                        if (key == null)
                            Toast.makeText(this, "Couldn't get push key for user", Toast.LENGTH_SHORT).show()
                        else {
                            mDatabase.child("users").child(key).setValue(pair)
                            Toast.makeText(this, "general_pool added successfully", Toast.LENGTH_SHORT).show()
                        }*/
                    }
                }.addOnFailureListener{
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
        }
        builder.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
        builder.show()
    }

    fun loadInspectionDetailsFragment(inspection: Inspection) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_activity_fragment_container, InspectionDetailsFragment(inspection)).addToBackStack(null).commit()
    }

    fun loadRestaurantDetailFragment(restaurantAndKey: Pair<String, Restaurant>) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_activity_fragment_container, RestaurantDetailFragment(restaurantAndKey)).addToBackStack(null).commit()
    }

    override fun launchRestaurantRecyclerFragment(
        list: List<Pair<String, Restaurant>>,
        listener: RestaurantAdapter.ItemClickListener?
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_activity_fragment_container, RestaurantRecyclerFragment(list, listener)).addToBackStack("search_fragment").commit()
    }
}
