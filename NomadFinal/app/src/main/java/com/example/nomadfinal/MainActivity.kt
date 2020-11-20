package com.example.nomadfinal

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

//import com.example.nomadfinal.data.Request
//import com.example.nomadfinal.data.*

import android.view.MenuItem




class MainActivity : AppCompatActivity() {

//    override fun onCreate(savedInstanceState: Bundle?)
//    {
//        super.onCreate(savedInstanceState)
//      //  setContentView(R.layout.landing_page)
//        setContentView(R.layout.activity_landing_page)
//    }


    private var checkPoint = 1
    private lateinit var geocoder: Geocoder
    private lateinit var location: Address

    private val RC_SIGN_IN = 123 // some arbitrary code
    private var currentEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Choose authentication providers
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult( AuthUI.getInstance()
                                .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                .setAvailableProviders(providers).build(), RC_SIGN_IN)



//        signOut.setOnClickListener{
//
//            Log.d("logout", "You've clicked log out")
//
//            AuthUI.getInstance().signOut(this).addOnCompleteListener {
//
//                val intent = Intent(this, landing_page::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
//                finish()
//
//            }
//
//        }

       ///////DEERAJ////////

        val addBut = findViewById<Button>(R.id.addCheckPoint)
        val remBut = findViewById<Button>(R.id.removeCheckPoint)
        val check1 = findViewById<TextInputLayout>(R.id.check1)
        val check2 = findViewById<TextInputLayout>(R.id.check2)
        val check3 = findViewById<TextInputLayout>(R.id.check3)
        val check4 = findViewById<TextInputLayout>(R.id.check4)
        val check5 = findViewById<TextInputLayout>(R.id.check5)
        val submitBut = findViewById<Button>(R.id.btnSubmit)
        val startPoint = findViewById<TextInputEditText>(R.id.startText)
        val endPoint = findViewById<TextInputEditText>(R.id.endText)

        addBut.setOnClickListener {
            when (checkPoint){
                1-> {
                    check1.visibility = View.VISIBLE
                    checkPoint++
                }

                2-> {
                    check2.visibility = View.VISIBLE
                    checkPoint++
                }

                3-> {
                    check3.visibility = View.VISIBLE
                    checkPoint++
                }

                4-> {
                    check4.visibility = View.VISIBLE
                    checkPoint++
                }

                5-> {
                    check5.visibility = View.VISIBLE
                    checkPoint++
                }

                6-> {
                    Toast.makeText(this, "Cannot add more checkpoints", Toast.LENGTH_SHORT).show()
                }
            }
        }

        remBut.setOnClickListener {
            when (checkPoint){
                1-> {
                    Toast.makeText(this, "No checkpoints to remove", Toast.LENGTH_SHORT).show()
                }

                2-> {
                    check1.visibility = View.INVISIBLE
                    checkPoint--
                }

                3-> {
                    check2.visibility = View.INVISIBLE
                    checkPoint--
                }

                4-> {
                    check3.visibility = View.INVISIBLE
                    checkPoint--
                }

                5-> {
                    check4.visibility = View.INVISIBLE
                    checkPoint--
                }

                6-> {
                    check5.visibility = View.INVISIBLE
                    checkPoint--
                }
            }
        }


        geocoder = Geocoder(this)

        var lat = 0.00
        var long = 0.00

        submitBut.setOnClickListener {
            if(!startPoint.text.isNullOrEmpty()){
                try{
                    location = geocoder.getFromLocationName(startPoint.text.toString(), 1).get(0)
                    if(location != null){
                        lat = location.latitude
                        long = location.longitude

                    }
                }
                catch (e: Exception){
                    Toast.makeText(this, "Please enter a valid Address!", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this, "Address is blank!", Toast.LENGTH_SHORT).show()
            }



//            if(lat!=0.00 && long!=0.00){
//
//                var background = object : Thread() {
//
//                     var url = "https://api.openweathermap.org/data/2.5/onecall?" + "appid=09f73a1fbf8932c02e6b56a252ac594f" + "lat=" + lat + "long=" + long
//
//                    Request(url).run()
//                }.start()
//
//
//            }

            if(lat!=0.00 && long!=0.00){

                object : Thread()  {

                    var url = "https://api.openweathermap.org/data/2.5/onecall?" + "lat=" + lat + "&lon=" + long + "&appid=09f73a1fbf8932c02e6b56a252ac594f"

                    override fun run() {
                        Request(url).run()
                    }

                }.start()

//                var url = "https://api.openweathermap.org/data/2.5/onecall?" + "lat=" + lat + "&lon=" + long + "&appid=09f73a1fbf8932c02e6b56a252ac594f"
//                Request(url).run()

            }
        }


        ///////DEERAJ//////

    }

    private fun formatTV(name: String, email: String): String {
        return """User: $name
                  Email: $email"""
    }


    private fun getEmail(email: String): String{
        return """Welcome, $email, where are we going today? """
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
       // return true

        return super.onCreateOptionsMenu(menu);


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.signOUT) {
            // do something here

            Log.d("idk", "you fucking clicked me!")

            AuthUI.getInstance().signOut(this).addOnCompleteListener {

                val intent = Intent(this, landing_page::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()

            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN)
        {
            // val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK)
            {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                val name = user?.displayName ?: ""
                currentEmail = user?.email ?: ""


                //greeting toast
                Toast.makeText(this, getEmail(currentEmail), Toast.LENGTH_SHORT).show()


                // userTV.text = formatTV(name, currentEmail)
                //temp
                //greeting.text = getEmail(currentEmail)
               //setContentView(R.layout.activity_main)

            }

            else
            {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d("ERROR", "Sign in failed")
            }
        }
    }
}
