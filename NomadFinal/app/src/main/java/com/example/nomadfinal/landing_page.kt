package com.example.nomadfinal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_landing_page.*

import android.widget.Button
class landing_page :  AppCompatActivity() {

    private val RC_SIGN_IN = 123 // some arbitrary code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())


        Login.setOnClickListener {

            Log.d("login", "you clicked login button!")

            setContentView(R.layout.activity_splash_page)

            startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                .setAvailableProviders(providers).build(), RC_SIGN_IN)

        }
    }

    private fun getEmail(email: String): String{
        return """Welcome, $email, where are we going today? """
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN)
        {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK)
            {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                val name = user?.displayName ?: ""
               // currentEmail = user?.email ?: ""

                val intent = Intent(this, MainActivity::class.java)
                    .putExtra("user", user!!.displayName)
                    .putExtra("email", user!!.email)
                startActivity(intent)
                finish()


                //greeting toast
                Toast.makeText(this, user!!.email?.let { getEmail(email = it) }, Toast.LENGTH_SHORT).show()

            }

            else
            {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.

                Log.d("ERROR", "Sign in failed")

                val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())


                if (response == null) {
                    // User pressed back button. NOTE: This is where the back action is
                    //taken care of

                    setContentView(R.layout.activity_landing_page)

                    val view = findViewById<Button>(R.id.Login)

                    view.setOnClickListener {

                        Log.d("login", "you clicked login button!")

                        setContentView(R.layout.activity_splash_page)

                        startActivityForResult(
                            AuthUI.getInstance()
                                .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                .setAvailableProviders(providers).build(), RC_SIGN_IN)

                    }

                    return;
                }

            }
        }
    }
}