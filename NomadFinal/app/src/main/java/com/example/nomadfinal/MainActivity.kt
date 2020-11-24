package com.example.nomadfinal

//import com.example.nomadfinal.data.Request
//import com.example.nomadfinal.data.*


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

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
    private var location1: MutableList<Address> = mutableListOf()
    private var locationLat: MutableList<Double> = mutableListOf()
    private var locationLong: MutableList<Double> = mutableListOf()
    private var timeStamp: MutableList<Long>  = mutableListOf()
    private lateinit var travelTime: String

    private val RC_SIGN_IN = 123 // some arbitrary code
    private var currentEmail = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Choose authentication providers
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(AuthUI.getInstance()
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

        /////DEERAJ/////----------------------------From

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
        val departTime = findViewById<Spinner>(R.id.spinner1)
        val departDate = findViewById<Spinner>(R.id.spinner2)

        var locationList = listOf(check1, check2, check3, check4, check5)

        addBut.setOnClickListener {
            when (checkPoint) {
                1 -> {
                    check1.visibility = View.VISIBLE
                    checkPoint++
                }

                2 -> {
                    check2.visibility = View.VISIBLE
                    checkPoint++
                }

                3 -> {
                    check3.visibility = View.VISIBLE
                    checkPoint++
                }

                4 -> {
                    check4.visibility = View.VISIBLE
                    checkPoint++
                }

                5 -> {
                    check5.visibility = View.VISIBLE
                    checkPoint++
                }

                6 -> {
                    Toast.makeText(this, "Cannot add more checkpoints", Toast.LENGTH_SHORT).show()
                }
            }
        }

        remBut.setOnClickListener {
            when (checkPoint) {
                1 -> {
                    Toast.makeText(this, "No checkpoints to remove", Toast.LENGTH_SHORT).show()
                }

                2 -> {
                    check1.visibility = View.INVISIBLE
                    checkPoint--
                }

                3 -> {
                    check2.visibility = View.INVISIBLE
                    checkPoint--
                }

                4 -> {
                    check3.visibility = View.INVISIBLE
                    checkPoint--
                }

                5 -> {
                    check4.visibility = View.INVISIBLE
                    checkPoint--
                }

                6 -> {
                    check5.visibility = View.INVISIBLE
                    checkPoint--
                }
            }
        }


        geocoder = Geocoder(this)

//        var start_lat = 0.00
//        var start_long = 0.00
//        var end_lat = 0.00
//        var end_long = 0.00

        submitBut.setOnClickListener {
            val dT = departTime.selectedItem
            val dD = departDate.selectedItem
            val epoch = SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse("$dD $dT:00").time / 1000

            timeStamp.add(epoch)

            //location1.add()

//            if(!startPoint.text.isNullOrEmpty() && !endPoint.text.isNullOrEmpty()){
//                try{
//                    location = geocoder.getFromLocationName(startPoint.text.toString(), 1).get(0)
//                    if(location != null){
//                        start_lat = location.latitude
//                        start_long = location.longitude
//
//
//
//                        location = geocoder.getFromLocationName(endPoint.text.toString(), 1).get(0)
//
//                        if(location != null){
//                            end_lat = location.latitude
//                            end_long = location.longitude
//                            CoroutineScope(IO).launch {
//                                if(checkCountry(start_lat, start_long) == true && checkCountry(end_lat, end_long) == true) {
//                                    mapApiRequest(start_lat, start_long, end_lat, end_long)
//                                }
//                                else{
//                                    Looper.prepare() // to be able to make toast
//                                    Toast.makeText(this@MainActivity, "Road trip within the US only!", Toast.LENGTH_LONG).show()
//                                    Looper.loop()
//                                }
//                            }
//                        }
//                        else
//                        {
//                            Toast.makeText(this, "End Point is not Valid!", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(this, "Start Point is not Valid!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                catch (e: Exception){
//                    Toast.makeText(this, "Please enter a valid Address!", Toast.LENGTH_SHORT).show()
//                }
//            }
            if(!startPoint.text.isNullOrEmpty() && !endPoint.text.isNullOrEmpty()){
                try{
                    location = geocoder.getFromLocationName(startPoint.text.toString(), 1).get(0)
                    if(location != null){

                        location1.add(location)

                        location = geocoder.getFromLocationName(endPoint.text.toString(), 1).get(0)

                        if(location != null){
                            location1.add(location)
                        }
                        else
                        {
                            Toast.makeText(this, "End Point is not Valid!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Start Point is not Valid!", Toast.LENGTH_SHORT).show()
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

            for(i in 1 until checkPoint){
                if(locationList[i-1].editText.toString().isNotEmpty()){
                    try{
                        location = geocoder.getFromLocationName(locationList[i-1].editText.toString(), 1).get(0)
                        if(location != null){
                            location1.add(location)
                        }
                        else{
                            Toast.makeText(this, "Please enter a valid Address!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch(e: Exception){
                        Toast.makeText(this, "Please enter a valid Address!", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "Address is blank!", Toast.LENGTH_SHORT).show()
                }
            }

            if(location1.size != checkPoint+1){
                Toast.makeText(this, "One or more addresses are Invalid!", Toast.LENGTH_SHORT).show()
            }

            else{
                var checkUSA = 0
                for(item in location1){
                    locationLat.add(item.latitude)
                    locationLong.add(item.longitude)
                    val job1 = CoroutineScope(IO).launch {
                        if(!checkCountry(item.latitude, item.longitude)){
                            checkUSA++
                        }
                    }

                    runBlocking{
                        job1.join()
                    }
                }
                if(checkUSA != 0){
                    Toast.makeText(this, "Road trip within the US only!", Toast.LENGTH_LONG).show()
                }
                else{
                    var checkTravelTime = 0
                    for(i in 1 .. checkPoint){
                        val job2 = CoroutineScope(IO).launch {
                            if(!mapApiRequest(locationLat[i-1], locationLong[i-1], locationLat[i], locationLong[i]))
                            {
                                checkTravelTime++
                            }
                        }

                        runBlocking{
                            job2.join()
                        }
                    }

                    if(checkTravelTime !=0){
                        Toast.makeText(this, "Road trip not possible!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        //val x = timeStamp

                        //Toast.makeText(this@MainActivity, "after"+timeStamp.size.toString(), Toast.LENGTH_LONG).show()


                        //Toast.makeText(this, "after"+timeStamp[1].toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            }






        }


        /////DEERAJ/////----------------------------To


        val times = resources.getStringArray(R.array.Times)

        // access the spinner
        //val spinner = findViewById<Spinner>(R.id.spinner1)
        if (spinner1 != null)
        {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)

            spinner1.adapter = adapter

            spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
                {
                    //  Toast.makeText(this@MainActivity, getString(R.string.selected_item) + " " + "" + languages[position], Toast.LENGTH_SHORT).show()
                    Log.d("timePick", times[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        val dateFormat1: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        var idk1 = dateFormat1.format(Date())

        val dateFormat2: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        var idk2 = dateFormat2.format(Date().time + 86400000)

        val dateFormat3: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        var idk3 = dateFormat3.format(Date().time + 2 * 86400000)

        val dateFormat4: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        var idk4 = dateFormat4.format(Date().time + 3 * 86400000)

        val dateFormat5: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        var idk5 = dateFormat5.format(Date().time + 4 * 86400000)

        // you need to have a list of data that you want the spinner to display
        // you need to have a list of data that you want the spinner to display
        val spinnerArray: MutableList<String> = ArrayList()

        spinnerArray.add(idk1)
        spinnerArray.add(idk2)
        spinnerArray.add(idk3)
        spinnerArray.add(idk4)
        spinnerArray.add(idk5)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArray)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val sItems = spinner2 as Spinner
        sItems.adapter = adapter

        sItems.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                Log.d("dayPick", spinnerArray[position])

            }

            override fun onNothingSelected(parent: AdapterView<*>)
            {
                // write code to perform some action
            }
        }
    }

    /////DEERAJ/////----------------------------From

    private suspend fun mapApiRequest(s_lat: Double, s_long: Double, e_lat: Double, e_long: Double): Boolean {
        var check = true
        withContext(IO){
            val job = launch {
                val time = measureTimeMillis {
                    try{
                        val result = JSONObject(getTravelTime(s_lat, s_long, e_lat, e_long).optString("route")).optString("formattedTime")
                        travelTime = result
                    }
                    catch (e: java.lang.Exception){
                        Looper.prepare() // to be able to make toast
                        Toast.makeText(this@MainActivity, "Road trip not possible!", Toast.LENGTH_LONG).show()
                        Looper.loop()
                        check = false
                    }
                    if(travelTime == ""){
                        Looper.prepare() // to be able to make toast
                        Toast.makeText(this@MainActivity, "Road trip not possible!", Toast.LENGTH_LONG).show()
                        Looper.loop()
                        check = false
                    }
                    else{
                        val previousTime = (timeStamp.last())

                        val hoursMin = travelTime.split(":")

                        val newTime = previousTime + ((hoursMin[0].toLong()) *  60 * 60) + (hoursMin[1].toLong() * 60)

//                        Looper.prepare() // to be able to make toast
//                        Toast.makeText(this@MainActivity, timeStamp.size.toString(), Toast.LENGTH_SHORT).show()
//                        Looper.loop()

                        timeStamp.add(newTime)

//                        Looper.prepare() // to be able to make toast
//                        Toast.makeText(this@MainActivity, "before"+timeStamp.size.toString(), Toast.LENGTH_SHORT).show()
//                        Looper.loop()

//                        if(timeStamp.size == 2)
//                        {
//                            Looper.prepare() // to be able to make toast
//                            Toast.makeText(this@MainActivity, "Added second Time Stamp!", Toast.LENGTH_LONG).show()
//                            Looper.loop()
//                        }
                    }
                }
            }
        }
        return check
    }

    private suspend fun checkCountry(lat: Double, long: Double):Boolean{
        var check = false
        withContext(IO){
            val job = launch {
                val time = measureTimeMillis {
                    var result = ""
                    try{
                        result = getCountryName(lat, long).getString("countryCode")
                    }
                    catch (e: Exception) {
                    }
                    if(result == "US") {
                        check = true
                    }
                }
            }
        }
        return check
    }

    private suspend fun getTravelTime(s_lat: Double, s_long: Double, e_lat: Double, e_long: Double): JSONObject {
        return JSONObject(Request("http://www.mapquestapi.com/directions/v2/route?key=A4UUgYYVFNvhyO0HK2vJWPAjjBYHTsGv&from=$s_lat,$s_long&to=$e_lat,$e_long").run())
    }

    private suspend fun getCountryName(lat: Double, long: Double): JSONObject {
        return JSONObject(Request("http://api.geonames.org/countryCodeJSON?lat=$lat&lng=$long&username=asuglio").run())
    }

    private suspend fun getWeatherInfo(lat: Double, long: Double): JSONObject {
        return JSONObject(Request("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&appid=09f73a1fbf8932c02e6b56a252ac594f").run())
    }

    /////DEERAJ/////------------------------------------To


//    private fun formatTV(name: String, email: String): String {
//        return """User: $name
//                  Email: $email"""
//    }

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

            Log.d("idk", "you definitely clicked me!")

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
