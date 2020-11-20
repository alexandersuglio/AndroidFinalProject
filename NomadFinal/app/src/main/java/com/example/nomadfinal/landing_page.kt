package com.example.nomadfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_landing_page.*

class landing_page :  AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        Login.setOnClickListener {

            Log.d("login", "you clicked login button!")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}