package com.example.nomadfinal

import android.content.Intent
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.nomadfinal.data.Data
import com.firebase.ui.auth.AuthUI
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

class HomeFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels()

    //don't tweak this...
    companion object
    {
        fun newInstance(): HomeFragment
        {
            return HomeFragment()
        }
    }


    private var checkPoint = 1
    private lateinit var geocoder: Geocoder
    private lateinit var location: Address
    private var location1: MutableList<Address> = mutableListOf()
    private var locationLat: MutableList<Double> = mutableListOf()
    private var locationLong: MutableList<Double> = mutableListOf()
    private var locality: MutableList<String> = mutableListOf()
    private var timeStamp: MutableList<Long>  = mutableListOf()
    private var dataList:MutableList<Data> = mutableListOf()
    private lateinit var travelTime: String
    private val sysTimeZone = Calendar.getInstance().timeZone.displayName


    //private val RC_SIGN_IN = 123 // some arbitrary code
    private var currentEmail = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        //inflate fragement layout gives me a view
        //then return view....
        var view = inflater.inflate(R.layout.activity_main, container, false)

        val addBut = view.findViewById<Button>(R.id.addCheckPoint)
        val remBut = view.findViewById<Button>(R.id.removeCheckPoint)
        val check_1 = view.findViewById<TextInputEditText>(R.id.check_1)
        val check_2 = view.findViewById<TextInputEditText>(R.id.check_2)
        val check_3 = view.findViewById<TextInputEditText>(R.id.check_3)
        val check_4 = view.findViewById<TextInputEditText>(R.id.check_4)
        val check_5 = view.findViewById<TextInputEditText>(R.id.check_5)
        val check1 = view.findViewById<TextInputLayout>(R.id.check1)
        val check2 = view.findViewById<TextInputLayout>(R.id.check2)
        val check3 = view.findViewById<TextInputLayout>(R.id.check3)
        val check4 = view.findViewById<TextInputLayout>(R.id.check4)
        val check5 = view.findViewById<TextInputLayout>(R.id.check5)
        val departText = view.findViewById<TextView>(R.id.leaving)
        val submitBut = view.findViewById<Button>(R.id.btnSubmit)
        val startPoint = view.findViewById<TextInputEditText>(R.id.startText)
        val endPoint = view.findViewById<TextInputEditText>(R.id.endText)
        val startLayout = view.findViewById<TextInputLayout>(R.id.startField)
        val endLayout = view.findViewById<TextInputLayout>(R.id.endField)
        val departTime = view.findViewById<Spinner>(R.id.spinner1)
        val departDate = view.findViewById<Spinner>(R.id.spinner2)
        view.findViewById<TextView>(R.id.timeZoneText)?.text  = sysTimeZone
        var locationList = listOf(check_1, check_2, check_3, check_4, check_5)

        setHasOptionsMenu(true);

        startLayout.visibility = View.VISIBLE
        endLayout.visibility = View.VISIBLE

        departText.text = "Depart Time:"
        departText.setTypeface(null, Typeface.BOLD)

        addBut?.setOnClickListener {
            when (checkPoint) {
                1 -> {
                    if (check1 != null) {
                        check1.visibility = View.VISIBLE
                    }
                    checkPoint++
                }

                2 -> {
                    if (check2 != null) {
                        check2.visibility = View.VISIBLE
                    }
                    checkPoint++
                }

                3 -> {
                    if (check3 != null) {
                        check3.visibility = View.VISIBLE
                    }
                    checkPoint++
                }

                4 -> {
                    if (check4 != null) {
                        check4.visibility = View.VISIBLE
                    }
                    checkPoint++
                }

                5 -> {
                    if (check5 != null) {
                        check5.visibility = View.VISIBLE
                    }
                    checkPoint++
                }

                6 -> {
                    Toast.makeText(
                        this.context,
                        "Cannot add more checkpoints",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        remBut?.setOnClickListener {
            when (checkPoint) {
                1 -> {
                    Toast.makeText(this.context, "No checkpoints to remove", Toast.LENGTH_LONG)
                        .show()
                }

                2 -> {
                    if (check1 != null) {
                        check1.visibility = View.INVISIBLE
                    }
                    checkPoint--
                }

                3 -> {
                    if (check2 != null) {
                        check2.visibility = View.INVISIBLE
                    }
                    checkPoint--
                }

                4 -> {
                    if (check3 != null) {
                        check3.visibility = View.INVISIBLE
                    }
                    checkPoint--
                }

                5 -> {
                    if (check4 != null) {
                        check4.visibility = View.INVISIBLE
                    }
                    checkPoint--
                }

                6 -> {
                    if (check5 != null) {
                        check5.visibility = View.INVISIBLE
                    }
                    checkPoint--
                }
            }
        }


        geocoder = Geocoder(this.context)

        submitBut?.setOnClickListener {
            val dT = departTime?.selectedItem
            val dD = departDate?.selectedItem
            val epoch = SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse("$dD $dT:00").time / 1000

            timeStamp.add(epoch)

            if (startPoint != null) {
                if (endPoint != null) {
                    if(!startPoint.text.isNullOrEmpty() && !endPoint.text.isNullOrEmpty()){
                        try{
                            location = geocoder.getFromLocationName(
                                startPoint.text.toString(),
                                1
                            )[0]
                            if(location != null){

                                location1.add(location)

                                location = geocoder.getFromLocationName(
                                    endPoint.text.toString(),
                                    1
                                )[0]

                                if(location != null){
                                    location1.add(location)
                                } else {
                                    Toast.makeText(
                                        this.context,
                                        "End Point is not Valid!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    clearVariables()
                                }
                            } else {
                                Toast.makeText(
                                    this.context,
                                    "Start Point is not Valid!",
                                    Toast.LENGTH_LONG
                                ).show()
                                clearVariables()
                            }
                        } catch (e: Exception){
                            Toast.makeText(
                                this.context,
                                "Please enter a valid Address!",
                                Toast.LENGTH_LONG
                            ).show()
                            clearVariables()
                        }

                    } else {
                        Toast.makeText(this.context, "Address is blank!", Toast.LENGTH_LONG).show()
                        clearVariables()
                    }
                }
            }

            for(i in 1 until checkPoint){
                if(locationList[i - 1]?.text.toString().isNotEmpty()){
                    Log.d("I am here", location1.size.toString() + "/" + checkPoint.toString())

                    try{
                        location = geocoder.getFromLocationName(
                            locationList[i - 1]?.text.toString(),
                            1
                        )[0]
                        if(location != null){
                            location1.add(location)
                        } else{
                            Toast.makeText(
                                this.context,
                                "Please enter a valid Address!",
                                Toast.LENGTH_LONG
                            ).show()
                            clearVariables()
                        }
                    } catch (e: Exception){
                        Toast.makeText(
                            this.context,
                            "Please enter a valid Address!",
                            Toast.LENGTH_LONG
                        ).show()
                        clearVariables()
                    }
                } else{
                    Toast.makeText(this.context, "Address is blank!", Toast.LENGTH_LONG).show()
                    clearVariables()
                }
            }

            if(location1.size != checkPoint+1){
                Log.d("error", location1.size.toString() + "/" + checkPoint.toString())
                Toast.makeText(
                    this.context,
                    "One or more addresses are Invalid!",
                    Toast.LENGTH_LONG
                ).show()
                clearVariables()
            } else{
                var checkUSA = 0
                for(item in location1){
                    locationLat.add(item.latitude)
                    locationLong.add(item.longitude)
                    locality.add(item.locality)
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
                    Toast.makeText(
                        this.context,
                        "Road trip within the US only!",
                        Toast.LENGTH_LONG
                    ).show()
                    clearVariables()
                } else{
                    var checkTravelTime = 0
                    for(i in 1 .. checkPoint){
                        val job2 = CoroutineScope(IO).launch {
                            if(!mapApiRequest(
                                    locationLat[i - 1],
                                    locationLong[i - 1],
                                    locationLat[i],
                                    locationLong[i]
                                )) {
                                checkTravelTime++
                            }
                        }

                        runBlocking{
                            job2.join()
                        }
                    }

                    if(checkTravelTime !=0){
                        Toast.makeText(
                            this.context,
                            "Road trip not possible!",
                            Toast.LENGTH_LONG
                        ).show()
                        clearVariables()
                    } else{

                        if(!(timeStamp.size == locationLat.size && timeStamp.size == locationLong.size && timeStamp.size == locality.size)){
                            Toast.makeText(
                                this.context,
                                "Please fill the details correctly!",
                                Toast.LENGTH_LONG
                            ).show()
                            clearVariables()
                        } else {
                            var checkWeatherInfo = 0
                            loop@ for(i in 0 until timeStamp.size){
                                val job3 = CoroutineScope(IO).launch {
                                    checkWeatherInfo = weatherForecast(
                                        locationLat[i],
                                        locationLong[i],
                                        locality[i],
                                        timeStamp[i]
                                    )
                                }
                                runBlocking{
                                    job3.join()
                                }
                                if(checkWeatherInfo == 1 || checkWeatherInfo == 2 || checkWeatherInfo == 5)
                                    break@loop
                            }

                            when(checkWeatherInfo){
                                0 -> {
                                    Toast.makeText(
                                        this.context,
                                        "Weather Information cannot be found",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    clearVariables()
                                }
                                1 -> {
                                    Toast.makeText(
                                        this.context,
                                        "Weather Information cannot be found.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    clearVariables()
                                }
                                2 -> {
                                    Toast.makeText(
                                        this.context,
                                        "Please provide future departure time!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    clearVariables()
                                }
                                5 -> {
                                    Toast.makeText(
                                        this.context,
                                        "Weather Information cannot be found.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    clearVariables()
                                }
                                else -> {

                                    Toast.makeText(
                                        this.context,
                                        "Successfully loaded Data!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    Log.d("listLoad", dataList.toString())

                                    viewModel.weatherInfo.postValue(dataList)

                                    clearVariables()


                                    parentFragmentManager.beginTransaction()
                                            .replace(R.id.main_frame, WeatherList())
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                            .addToBackStack(null)
                                            .commit()


                                    ////////

                                    //new intent.....
                                    //new page
                                    //display all the info

                                    //on click specific result
                                    //single view

                                    //   var test = dataList.toTypedArray()

//                                    var view = inflater.inflate(R.layout.weather_row, container, false)
//                                    var title = activity?.findViewById<TextView>(R.id.subRowHeading)

    //                                        viewModel.observeWeather().observe(viewLifecycleOwner, androidx.lifecycle.Observer { data ->
    //                                            if (title != null)
    //                                            {
    //                                                title.text = data.toString()
    //                                            }
    //                                        })


                                    // var test = dataList.map { it as Data }.toTypedArray()


                                    // var test = dataList.toCollection(ArrayList())
                                    //                                    Log.d("works", test.toString())
                                    //
                                    //
    //                                        val weatherIntent = Intent(this.context, WeatherPost::class.java).putExtra("weatherData", test)
    //                                        startActivity(weatherIntent)
    //                                        finish()


                                    //                                    WeatherPost weatherPost //where data needs to be pass
                                    //                                            Bundle bundle = new Bundle()
                                    //                                    bundle.putParcelableArrayList("data", dataList);
                                    //                                    weatherPost.setArguments(bundle);


                                    //  viewModel.weatherInfo.postValue(dataList)


                                    ////////



                                }
                            }
                        }
                    }
                }
            }


        }


        /////DEERAJ/////----------------------------To


        val times = resources.getStringArray(R.array.Times)

        // access the spinner
        //val spinner = findViewById<Spinner>(R.id.spinner1)
        if (departTime != null)
        {
            val adapter =
                this.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, times) }

            departTime.adapter = adapter

            departTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                )
                {
                    //  Toast.makeText(this@MainActivity, getString(R.string.selected_item) + " " + "" + languages[position], Toast.LENGTH_LONG).show()
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

        val adapter = this.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, spinnerArray) }

        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val sItems = departDate as? Spinner
        if (sItems != null) {
            sItems.adapter = adapter
        }

        if (sItems != null) {
            sItems.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                )
                {
                    Log.d("dayPick", spinnerArray[position])

                }

                override fun onNothingSelected(parent: AdapterView<*>)
                {
                    // write code to perform some action
                }
            }
        }

        Log.d("datalist", dataList.toString())

        return view
    }

    /////DEERAJ/////----------------------------From

    private suspend fun mapApiRequest(s_lat: Double, s_long: Double, e_lat: Double, e_long: Double): Boolean {
        var check = true
        withContext(IO){
            val job = launch {
                val time = measureTimeMillis {
                    try{
                        val result = JSONObject(
                            getTravelTime(s_lat, s_long, e_lat, e_long).optString(
                                "route"
                            )
                        ).optString("formattedTime")
                        travelTime = result
                    }
                    catch (e: java.lang.Exception){
                        Looper.prepare() // to be able to make toast
                        Toast.makeText(

                            //not sure
                            parentFragment?.context,
                            "Road trip not possible!",
                            Toast.LENGTH_LONG
                        ).show()
                        Looper.loop()
                        clearVariables()
                        check = false
                    }
                    if(travelTime == ""){
                        Looper.prepare() // to be able to make toast
                        Toast.makeText(
                            parentFragment?.context,

                            "Road trip not possible!",
                            Toast.LENGTH_LONG
                        ).show()
                        Looper.loop()
                        clearVariables()
                        check = false
                    }
                    else{
                        val previousTime = (timeStamp.last())

                        val hoursMin = travelTime.split(":")

                        val newTime = previousTime + ((hoursMin[0].toLong()) *  60 * 60) + (hoursMin[1].toLong() * 60)

//                        Looper.prepare() // to be able to make toast
//                        Toast.makeText(this@MainActivity, timeStamp.size.toString(), Toast.LENGTH_LONG).show()
//                        Looper.loop()

                        timeStamp.add(newTime)

//                        Looper.prepare() // to be able to make toast
//                        Toast.makeText(this@MainActivity, "before"+timeStamp.size.toString(), Toast.LENGTH_LONG).show()
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

    private suspend fun weatherForecast(
        lat: Double,
        long: Double,
        locality: String,
        timeValue: Long
    ):Int{
        var check = 1
        withContext(IO){
            val job = launch {
                val time = measureTimeMillis {
                    var tempKelvin: Float? = null
                    var tempF: Int? = null
                    var weatherCondition = ""
                    var icon = ""
                    var time = ""
                    var timeZone = ""
                    var offset = 0L
                    try{
                        val current = System.currentTimeMillis()/1000

                        if(timeValue < current)
                        {
                            check = 2
                        }
                        else if(timeValue == current || timeValue <= current+3000){
                            val jsonDataObject = getWeatherInfo(lat, long)
                            tempKelvin = JSONObject(jsonDataObject.getString("current")).optString(
                                "temp"
                            ).toFloat()

                            offset = jsonDataObject.getString("timezone_offset").toLong()

                            timeZone = jsonDataObject.getString("timezone")

                            weatherCondition = JSONObject(
                                JSONObject(jsonDataObject.getString("current")).optString(
                                    "weather"
                                ).replace("[", "").replace("]", "")
                            ).optString("main")

                            icon = JSONObject(
                                JSONObject(jsonDataObject.getString("current")).optString(
                                    "weather"
                                ).replace("[", "").replace("]", "")
                            ).optString("icon")

                            time = java.time.format.DateTimeFormatter.ISO_INSTANT
                                .format(java.time.Instant.ofEpochSecond(timeValue + offset))

                            tempF = ((tempKelvin - 273.15) * 9/5 + 32).toFloat().roundToInt()
                            dataList.add(
                                Data(
                                    locality,
                                    tempF,
                                    weatherCondition,
                                    icon,
                                    time,
                                    timeZone
                                )
                            )
                            check = 3
                        }
                        else if(timeValue > current+3000 && timeValue <= current + 165600){
                            val roundUp = (timeValue - (timeValue % 3600) + 3600).toString()
                            val jsonDataObject = getWeatherInfo(lat, long)
                            val temp = jsonDataObject.getJSONArray("hourly")

                            for(i in 0 until temp.length()){
                                val newJson = JSONObject(temp[i].toString())
                                if(newJson.getString("dt")==roundUp){
                                    offset = jsonDataObject.getString("timezone_offset").toLong()

                                    timeZone = jsonDataObject.getString("timezone")

                                    weatherCondition = JSONObject(
                                        (newJson.getString("weather")).replace(
                                            "[",
                                            ""
                                        ).replace("]", "")
                                    ).optString("main")

                                    icon = JSONObject(
                                        (newJson.getString("weather")).replace(
                                            "[",
                                            ""
                                        ).replace("]", "")
                                    ).optString("icon")


                                    time = java.time.format.DateTimeFormatter.ISO_INSTANT
                                        .format(java.time.Instant.ofEpochSecond(timeValue + offset))

                                    tempKelvin = newJson.getString("temp").toFloat()

                                    tempF = ((tempKelvin - 273.15) * 9/5 + 32).toFloat().roundToInt()
                                    dataList.add(
                                        Data(
                                            locality,
                                            tempF,
                                            weatherCondition,
                                            icon,
                                            time,
                                            timeZone
                                        )
                                    )
                                    check = 3
                                    break
                                }
                            }
                        }
                        else if(timeValue > current + 165600 && timeValue <= current + 777600){
                            //var roundUp = (timeValue - (timeValue % 86400) + 86400)
                            val jsonDataObject = getWeatherInfo(lat, long)
                            val temp = jsonDataObject.getJSONArray("daily")

                            val firstDate = JSONObject(temp[0].toString()).getString("dt").toLong()

                            var roundUp = firstDate

                            if(timeValue <= firstDate)
                            {
                                roundUp = firstDate
                            }
                            else {
                                val uy = timeValue - firstDate
                                val yt = uy / 86400
                                val some = (yt * 86400)
                                roundUp = some + firstDate
                            }

                            for(i in 0 until temp.length()){
                                val newJson = JSONObject(temp[i].toString())
                                if(newJson.getString("dt") == roundUp.toString()){
                                    offset = jsonDataObject.getString("timezone_offset").toLong()

                                    timeZone = jsonDataObject.getString("timezone")

                                    weatherCondition = JSONObject(
                                        (newJson.getString("weather")).replace(
                                            "[",
                                            ""
                                        ).replace("]", "")
                                    ).optString("main")

                                    icon = JSONObject(
                                        (newJson.getString("weather")).replace(
                                            "[",
                                            ""
                                        ).replace("]", "")
                                    ).optString("icon")


                                    time = java.time.format.DateTimeFormatter.ISO_INSTANT
                                        .format(java.time.Instant.ofEpochSecond(timeValue + offset))

                                    tempKelvin = JSONObject(newJson.getString("temp")).getString("day").toFloat()

                                    tempF = ((tempKelvin - 273.15) * 9/5 + 32).toFloat().roundToInt()
                                    dataList.add(
                                        Data(
                                            locality,
                                            tempF,
                                            weatherCondition,
                                            icon,
                                            time,
                                            timeZone
                                        )
                                    )
                                    check = 3
                                    break
                                }
                                else{
                                    check = 5
                                }
                            }
                        }
                        else{

                            val jsonDataObject = getWeatherInfo(lat, long)

                            offset = jsonDataObject.getString("timezone_offset").toLong()

                            timeZone = jsonDataObject.getString("timezone")


                            time = java.time.format.DateTimeFormatter.ISO_INSTANT
                                .format(java.time.Instant.ofEpochSecond(timeValue + offset))

                            dataList.add(
                                Data(
                                    locality,
                                    null,
                                    weatherCondition,
                                    icon,
                                    time,
                                    timeZone
                                )
                            )
                            check = 3
                        }
                    }
                    catch (e: Exception) {
                        check = 5
                    }
                }
            }
        }
        return check
    }

    private fun clearVariables() {
        location1 = mutableListOf()
        locationLat = mutableListOf()
        locationLong = mutableListOf()
        locality = mutableListOf()
        timeStamp = mutableListOf()
        dataList = mutableListOf()
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

//    private fun getEmail(email: String): String{
//        return """Welcome, $email, where are we going today? """
//    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu!!, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.signOUT) {
            // do something here

            Log.d("idk", "you definitely clicked me!")

            this.context?.let {

                AuthUI.getInstance().signOut(it).addOnCompleteListener {

                    val intent = Intent(this.context, landing_page::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)


                   //finish()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

}