package com.example.nomadfinal
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123 // some arbitrary code
    private var currentEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // setContentView(R.layout.activity_splash_page)

        // Choose authentication providers
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN)



//        setDisplayName.setOnClickListener {
//            val name = displayNameET.text.toString()
//            userTV.text = formatTV(name, currentEmail)
//
//            // Update the user's displayName on Firebase
//            // From https://firebase.google.com/docs/auth/android/manage-users#update_a_users_profile
//            val user = FirebaseAuth.getInstance().currentUser
//            val builder = UserProfileChangeRequest.Builder()
//            builder.setDisplayName(name)
//            val changeRequest = builder.build()
//            user?.updateProfile(changeRequest)?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d("FC6", "Updated display name to $name")
//                }
//            }
//        }



        //var userName = FirebaseAuth.getInstance().currentUser!!.displayName

      //  if (userName != null)
      //  {
      //      greeting.text = "welcome, " + userName + ", where are we going today?"
      //  }
      //  else
       // {
       //    greeting.text = FirebaseAuth.getInstance().currentUser!!.email
      //  }



        signOut.setOnClickListener{

            Log.d("logout", "You've clicked log out")

           // onStop()

            AuthUI.getInstance().signOut(this).addOnCompleteListener {

                //         var idk = Intent(this, SplashPage::class.java)
//            startActivity(idk)
//            finish()

                val intent = Intent(this, SplashPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()

            }
//
//            var idk = Intent(this, SplashPage::class.java)
//            startActivity(idk)
//            finish()



        }


    }

    private fun formatTV(name: String, email: String): String {
        return """User: $name
                  Email: $email"""
    }


    private fun getEmail(email: String): String{
        return """Welcome, $email, where are we going today? """
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

               // userTV.text = formatTV(name, currentEmail)
                greeting.text = getEmail(currentEmail)

                setContentView(R.layout.activity_main)


            }

            else
            {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d("FC6", "Sign in failed")
            }
        }
    }


 //   override fun onStop()
 //   {

//        AuthUI.getInstance().signOut(this).addOnCompleteListener {
//
//   //         var idk = Intent(this, SplashPage::class.java)
////            startActivity(idk)
////            finish()
//
//            val intent = Intent(this, SplashPage::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(intent)
//            finish()
//
//        }




   //     Firebase.auth.signOut()

//        AuthUI.getInstance()
//            .signOut(this)
//            .addOnCompleteListener {
//               currentEmail = ""
//              //  userTV.text = formatTV(" ", " ")
//                Log.d("Nomad", "Signed out of the app")
//            }

 //   super.onStop()
 //   }
}
