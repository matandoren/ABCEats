package com.example.android2finalproject.fragments.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.android2finalproject.R
import com.example.android2finalproject.activitys.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val mAuth = FirebaseAuth.getInstance()
    private val email ="timakudev@gmail.com"
    private val password ="123456"
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
        setHasOptionsMenu(true)


        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

        /*
        Both ways are equal the difference the first we find the ID
        the 2nd we can do the function thorough the id

        val restaurantName = view.findViewById<EditText>(R.id.enter_restaurant_name_fragment_main_editText)

        enter_restaurant_name_fragment_main_editText.setText("aloha")
        */

        // Initialize Firebase Auth


        val restaurantName = view.findViewById<EditText>(R.id.enter_restaurant_name_fragment_main_editText)
        val restaurantStreetName = view.findViewById<EditText>(R.id.enter_restaurant_street_fragment_main_editText)



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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.login_icon_1){
            Toast.makeText(context, "Sign in selected", Toast.LENGTH_LONG).show()
            dialogPopUP("Inspector Activity","would you like to open Inspector Activity?","FOR FUCK SAKE ITS WORKS!")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogPopUP(title:String, message:String, toast:String) {

        val builder = AlertDialog.Builder(this!!.context!!)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            /* checks email and password if empty
            if (email.isEmpty() || password.isEmpty())
                Toast.makeText(
                    context,
                    "The username and the password must not be empty",
                    Toast.LENGTH_LONG
                ).show()

                else*/
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                if(!it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
                    val mainActivity = activity as MainActivity?
                    mainActivity!!.openInspectorActivity()
                }
            }.addOnFailureListener{
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
        }
        builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sign_in_menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }
}
