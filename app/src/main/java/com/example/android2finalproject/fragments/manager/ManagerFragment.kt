package com.example.android2finalproject.fragments.manager

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.android2finalproject.R
import com.example.android2finalproject.activities.ManagerActivity
import com.example.android2finalproject.model.Restaurant
import com.example.android2finalproject.model.UsernameToRole
import com.example.android2finalproject.model.ViolationCategory
import com.example.android2finalproject.recycler_view_adapters.InspectorsRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ManagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManagerFragment : Fragment() {
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
        val view: View = inflater.inflate(R.layout.fragment_manager, container, false)

        val addRestaurant = view.findViewById<Button>(R.id.add_restaurant_fragment_manager_Button)
        addRestaurant.setOnClickListener { addRestaurant() }
        val addInspector = view.findViewById<Button>(R.id.add_inspector_fragment_manager_Button)
        addInspector.setOnClickListener { addInspector() }
        val addViolationCategory = view.findViewById<Button>(R.id.add_violation_category_fragment_manager_Button)
        addViolationCategory.setOnClickListener { addViolationCategory() }
        val addViolation = view.findViewById<Button>(R.id.add_violation_fragment_manager_Button)
        val assignInspector = view.findViewById<Button>(R.id.assign_inspector_fragment_manager_Button)
        val removeInspector = view.findViewById<Button>(R.id.remove_inspector_fragment_manager_Button)
        removeInspector.setOnClickListener { deleteInspector() }
        val removeRestaurant = view.findViewById<Button>(R.id.remove_restaurant_fragment_manager_Button)




        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun addInspector() {
        val mAuth = FirebaseAuth.getInstance()
        val mDatabase = FirebaseDatabase.getInstance().reference
        val builder = AlertDialog.Builder(this.context!!)
        val view = layoutInflater.inflate(R.layout.username_and_password_layout, null)
        builder.setTitle("Add inspector")
        builder.setView(view)
        builder.setPositiveButton("Ok") { _: DialogInterface, _: Int ->
            val usernameET = view.findViewById<EditText>(R.id.sign_in_username_ET)
            val passwordET = view.findViewById<EditText>(R.id.sign_in_password_ET)
            /* checks email and password if empty */
            if (usernameET.text.toString().isEmpty() || passwordET.text.toString().isEmpty())
                Toast.makeText(
                    context,
                    "The username and the password must not be empty",
                    Toast.LENGTH_LONG
                ).show()
            else
                mAuth.createUserWithEmailAndPassword(usernameET.text.toString(), passwordET.text.toString())
                    .addOnCompleteListener{
                        if(it.isSuccessful) {
                            // user added successfully to FirebaseAuth, now add it to the database with the role of an inspector
                            val username = usernameET.text.toString()
                            val pair = UsernameToRole(username, "inspector")
                            val key = mDatabase.child("users").push().key
                            if (key == null)
                                Toast.makeText(context, "Couldn't get push key for the user", Toast.LENGTH_SHORT).show()
                            else {
                                mDatabase.child("users").child(key).setValue(pair)
                                Toast.makeText(context, "Inspector added successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.addOnFailureListener{
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
        }
        builder.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
        builder.show()
    }

    private fun addRestaurant() {
        val builder = AlertDialog.Builder(this.context!!)
        val view = layoutInflater.inflate(R.layout.add_restaurant_layout, null)
        builder.setTitle("Add restaurant")
        builder.setView(view)
        builder.setPositiveButton("Ok") { _: DialogInterface, _: Int ->
            val nameET = view.findViewById<EditText>(R.id.add_restaurant_name_ET)
            val street1ET = view.findViewById<EditText>(R.id.add_restaurant_street1_ET)
            val number1ET = view.findViewById<EditText>(R.id.add_restaurant_number1_ET)
            val street2ET = view.findViewById<EditText>(R.id.add_restaurant_street2_ET)
            val number2ET = view.findViewById<EditText>(R.id.add_restaurant_number2_ET)
            val cityET = view.findViewById<EditText>(R.id.add_restaurant_city_ET)
            /* checks that name, city, street1 and number1  are not empty */
            if (nameET.text.toString().isEmpty() || cityET.text.toString().isEmpty() || street1ET.text.toString().isEmpty() || number1ET.text.toString().isEmpty())
                Toast.makeText(
                    context,
                    "The restaurant name, the city name, the street name and number must not be empty",
                    Toast.LENGTH_LONG
                ).show()
            else {
                val restaurant = Restaurant(nameET.text.toString(), cityET.text.toString(), street1ET.text.toString(), number1ET.text.toString().toInt())
                if (street2ET.text.toString().isNotEmpty() && number2ET.text.toString().isNotEmpty()) {
                    restaurant.street2 = street2ET.text.toString()
                    restaurant.house_number2 = number2ET.text.toString().toInt()
                }
                val calendar = Calendar.getInstance()
                restaurant.next_inspection_year = calendar.get(Calendar.YEAR)
                restaurant.next_inspection_month = calendar.get(Calendar.MONTH) + 1
                restaurant.next_inspection_day = calendar.get(Calendar.DAY_OF_MONTH)

                val managerActivity = activity as ManagerActivity?
                managerActivity!!.loadInspectorsRecyclerFragment(false, object: InspectorsRecyclerViewAdapter.ItemClickListener {
                    override fun onItemClick(username_clicked: String) {
                        AlertDialog.Builder(this@ManagerFragment.context!!).setTitle("Confirm assignment")
                            .setMessage("Are you sure you want to assign the inspector: $username_clicked to the restaurant: ${restaurant.name}?")
                            .setPositiveButton("Yes") {_: DialogInterface, _: Int ->
                                restaurant.assigned_inspector_username = username_clicked
                                val databaseRef = FirebaseDatabase.getInstance().reference
                                val key = databaseRef.child("restaurants").push().key
                                if (key == null)
                                    Toast.makeText(context, "Couldn't get push key for restaurant", Toast.LENGTH_SHORT).show()
                                else {
                                    databaseRef.child("restaurants").child(key).setValue(restaurant)
                                    Toast.makeText(context, "restaurant added successfully", Toast.LENGTH_SHORT).show()
                                    activity?.supportFragmentManager?.popBackStack()
                                }
                            }.setNegativeButton("No"){ _: DialogInterface, _: Int ->}.show()
                    }
                })
            }
        }
        builder.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
        builder.show()
    }

    private fun deleteInspector() {
        val managerActivity = activity as ManagerActivity?
        managerActivity!!.loadInspectorsRecyclerFragment(true, object: InspectorsRecyclerViewAdapter.ItemClickListener {
            override fun onItemClick(username_clicked: String) {
                AlertDialog.Builder(this@ManagerFragment.context!!).setTitle("Confirm removal")
                    .setMessage("Are you sure you want to delete the inspector: $username_clicked?")
                    .setPositiveButton("Yes") {_: DialogInterface, _: Int ->
                        // query the database for this user
                        val query = FirebaseDatabase.getInstance().reference.child("users").orderByChild("username").equalTo(username_clicked)
                        val queryListener = object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // Get the reference for that user
                                for (childSnapShot in dataSnapshot.children)  // it is assumed that there will be only one child
                                    childSnapShot.ref.removeValue()

                                val query2 = FirebaseDatabase.getInstance().reference.child("restaurants").orderByChild("assigned_inspector_username").equalTo(username_clicked)
                                query2.addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onCancelled(p0: DatabaseError) {
                                        Toast.makeText(context, "Failed to delete this user", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        for (css in p0.children) { // it is assumed that there will be only one child
                                            val restaurant = css.getValue(Restaurant::class.java)
                                            restaurant?.assigned_inspector_username = "general_pool"
                                            val key = css.key
                                            if (key != null)
                                                FirebaseDatabase.getInstance().reference.child("restaurants").child(key).setValue(restaurant)
                                        }
                                        Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show()
                                        activity?.supportFragmentManager?.popBackStack()
                                    }

                                })
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Toast.makeText(context, "Failed to delete this user", Toast.LENGTH_SHORT).show()
                            }
                        }
                        query.addListenerForSingleValueEvent(queryListener)
                    }.setNegativeButton("No"){ _: DialogInterface, _: Int ->}.show()
            }
        })
    }

    private fun addViolationCategory() {
        val inputET = EditText(context)
        inputET.hint = "Category name"

        AlertDialog.Builder(this@ManagerFragment.context!!).setTitle("Add violation category").setView(inputET)
            .setPositiveButton("Ok") {_: DialogInterface, _: Int ->
                if (inputET.text.toString().isEmpty())
                    Toast.makeText(context, "The category name must not be empty", Toast.LENGTH_LONG).show()
                else {
                    val violationCategory = ViolationCategory(inputET.text.toString())
                    val ref = FirebaseDatabase.getInstance().reference.child("violation_categories")
                    val key = ref.push().key
                    if (key == null)
                        Toast.makeText(context, "Couldn't get push key for violation category", Toast.LENGTH_SHORT).show()
                    else {
                        ref.child(key).setValue(violationCategory)
                        Toast.makeText(context, "violation category added successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }.setNegativeButton("Cancel"){ _: DialogInterface, _: Int ->}.show()
    }


}
