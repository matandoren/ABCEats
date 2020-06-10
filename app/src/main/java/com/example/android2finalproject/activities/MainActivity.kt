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
import com.example.android2finalproject.R
import com.example.android2finalproject.model.UsernameToRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val mDatabase = FirebaseDatabase.getInstance().reference
    //private val email ="timakudev@gmail.com"
    //private val password ="123456"

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
                                        openManagerActivity()
                                        return
                                    }
                                    if (pair?.role.equals("inspector")) {
                                        openInspectorActivity()
                                        return
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Toast.makeText(this@MainActivity, "Failed to retrieve the UserToRole object for this user", Toast.LENGTH_SHORT).show()
                            }
                        }
                        query.addListenerForSingleValueEvent(queryListener)

                        /*val username = usernameET.text.toString()
                        val pair = UsernameToRole(username, "manager")
                        val key = mDatabase.child("users").push().key
                        if (key == null)
                            Toast.makeText(this, "Couldn't get push key for user", Toast.LENGTH_SHORT).show()
                        else {
                            mDatabase.child("users").child(key).setValue(pair)
                            Toast.makeText(this, "Manager added successfully", Toast.LENGTH_SHORT).show()
                        }*/
                    }
                }.addOnFailureListener{
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
        }
        builder.setNegativeButton("Cancel") { _: DialogInterface, i: Int -> }
        builder.show()
    }
}
