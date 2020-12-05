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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nomadfinal.data.DailyWeather
import com.example.nomadfinal.data.Data
import com.example.nomadfinal.data.Request
import com.firebase.ui.auth.AuthUI
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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

    //Using val viewModel to post the Live data of Weather information to the MainViewModel
    private val viewModel: MainViewModel by activityViewModels()

    companion object
    {
        fun newInstance(): HomeFragment
        {
            return HomeFragment()
        }
    }

    //initializing the Member variables Required for getting the Weather and posting it to MainViewModel
    private var checkPoint = 1
    private lateinit var geocoder: Geocoder
    private lateinit var location: Address
    private var location1: MutableList<Address> = mutableListOf()
    private var locationLat: MutableList<Double> = mutableListOf()
    private var locationLong: MutableList<Double> = mutableListOf()
    private var locality: MutableList<String> = mutableListOf()
    private var timeStamp: MutableList<Long>  = mutableListOf()
    private var dataList:MutableList<Data> = mutableListOf()
    private var dailyWeather:MutableList<DailyWeather> = mutableListOf()
    private lateinit var travelTime: String
    private val sysTimeZone = Calendar.getInstance().timeZone.displayName

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        //inflate fragment layout gives a view
        val view = inflater.inflate(R.layout.activity_main, container, false)

        /////DEERAJ/////----------------------------From

        // Getting all the Text Fields and Buttons from the activity_main
        val addBut = view.findViewById<Button>(R.id.addCheckPoint)
        val remBut = view.findViewById<Button>(R.id.removeCheckPoint)
        val checkText1 = view.findViewById<TextInputEditText>(R.id.check_1)
        val checkText2 = view.findViewById<TextInputEditText>(R.id.check_2)
        val checkText3 = view.findViewById<TextInputEditText>(R.id.check_3)
        val checkText4 = view.findViewById<TextInputEditText>(R.id.check_4)
        val checkText5 = view.findViewById<TextInputEditText>(R.id.check_5)
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
        val locationList = listOf(checkText1, checkText2, checkText3, checkText4, checkText5)

        //Setting the option menu to true to make the Sign out button visible
        setHasOptionsMenu(true)

        //Setting the visibility of startPoint and EndPoint as these fields are always required.
        startLayout.visibility = View.VISIBLE
        endLayout.visibility = View.VISIBLE

        departText.text = "Depart Time:"
        departText.setTypeface(null, Typeface.BOLD)

        // When the back button is pressed in the Next Fragments, the text fields should
        // be visible based on the previously clicked buttons.
        when(checkPoint){
            1 -> {
                check1.visibility = View.INVISIBLE
                check2.visibility = View.INVISIBLE
                check3.visibility = View.INVISIBLE
                check4.visibility = View.INVISIBLE
                check5.visibility = View.INVISIBLE
            }
            2 -> {
                check1.visibility = View.VISIBLE
                check2.visibility = View.INVISIBLE
                check3.visibility = View.INVISIBLE
                check4.visibility = View.INVISIBLE
                check5.visibility = View.INVISIBLE
            }
            3 -> {
                check1.visibility = View.VISIBLE
                check2.visibility = View.VISIBLE
                check3.visibility = View.INVISIBLE
                check4.visibility = View.INVISIBLE
                check5.visibility = View.INVISIBLE
            }
            4 -> {
                check1.visibility = View.VISIBLE
                check2.visibility = View.VISIBLE
                check3.visibility = View.VISIBLE
                check4.visibility = View.INVISIBLE
                check5.visibility = View.INVISIBLE
            }
            5 -> {
                check1.visibility = View.VISIBLE
                check2.visibility = View.VISIBLE
                check3.visibility = View.VISIBLE
                check4.visibility = View.VISIBLE
                check5.visibility = View.INVISIBLE
            }
            6 -> {
                check1.visibility = View.VISIBLE
                check2.visibility = View.VISIBLE
                check3.visibility = View.VISIBLE
                check4.visibility = View.VISIBLE
                check5.visibility = View.VISIBLE
            }

        }

        // Handling the Add Stop Button based on the previous checkPoint value, the text filed will be made visible.
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

        // Handling the Add Stop Button based on the previous checkPoint value, the text filed will be made invisible.
        remBut?.setOnClickListener {
            when (checkPoint) {
                1 -> {
                    Toast.makeText(this.context, "No checkpoints to remove", Toast.LENGTH_LONG)
                            .show()
                }

                2 -> {
                    if (check1 != null) {
                        check1.visibility = View.INVISIBLE
                        checkText1.setText("")
                    }
                    checkPoint--
                }

                3 -> {
                    if (check2 != null) {
                        check2.visibility = View.INVISIBLE
                        checkText2.setText("")
                    }
                    checkPoint--
                }

                4 -> {
                    if (check3 != null) {
                        check3.visibility = View.INVISIBLE
                        checkText3.setText("")
                    }
                    checkPoint--
                }

                5 -> {
                    if (check4 != null) {
                        check4.visibility = View.INVISIBLE
                        checkText4.setText("")
                    }
                    checkPoint--
                }

                6 -> {
                    if (check5 != null) {
                        check5.visibility = View.INVISIBLE
                        checkText5.setText("")
                    }
                    checkPoint--
                }
            }
        }


        // Using geocoder object to get the location details of the typed Address
        geocoder = Geocoder(this.context)

        //Handling the onclicklistener for the button "Get Weather Info"
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

                            location1.add(location)

                            location = geocoder.getFromLocationName(
                                    endPoint.text.toString(),
                                    1
                            )[0]

                            location1.add(location)
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
                        location1.add(location)
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

                                    //alert.cancel()

                                    parentFragmentManager.beginTransaction()
                                            .replace(R.id.main_frame, WeatherList())
                                            .addToBackStack(null)
                                            .commit()

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
        if (departTime != null)
        {
            val adapter =
                    this.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, times) }

            departTime.adapter = adapter
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

        return view
    }

    /////DEERAJ/////----------------------------From

    private suspend fun mapApiRequest(s_lat: Double, s_long: Double, e_lat: Double, e_long: Double): Boolean {
        var check = true
        withContext(IO){
            launch {
                measureTimeMillis {
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
                                this@HomeFragment.context,
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
                                this@HomeFragment.context,
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

                        timeStamp.add(newTime)

                    }
                }
            }
        }
        return check
    }

    private suspend fun checkCountry(lat: Double, long: Double):Boolean{
        var check = false
        withContext(IO){
            launch {
                measureTimeMillis {
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
            launch {
                measureTimeMillis {
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

                            val temp1 = jsonDataObject.getJSONArray("daily")

                            for(i in 0 until temp1.length()){
                                val newJson = JSONObject(temp1[i].toString())

                                val weatherCondition1 = JSONObject(
                                        (newJson.getString("weather")).replace(
                                                "[",
                                                ""
                                        ).replace("]", "")
                                ).optString("main")

                                val icon1 = JSONObject(
                                        (newJson.getString("weather")).replace(
                                                "[",
                                                ""
                                        ).replace("]", "")
                                ).optString("icon")


                                val time1 = java.time.format.DateTimeFormatter.ISO_INSTANT
                                        .format(java.time.Instant.ofEpochSecond(newJson.getString("dt").toLong()))

                                val tempKelvinMax1 = JSONObject(newJson.getString("temp")).getString("max").toFloat()

                                val tempFMax1 = ((tempKelvinMax1 - 273.15) * 9/5 + 32).toFloat().roundToInt()

                                val tempKelvinMin1 = JSONObject(newJson.getString("temp")).getString("min").toFloat()

                                val tempFMin1 = ((tempKelvinMin1 - 273.15) * 9/5 + 32).toFloat().roundToInt()

                                dailyWeather.add(DailyWeather(tempFMin1, tempFMax1, weatherCondition1, icon1, time1, locality))
                            }




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
                                            timeZone,
                                            dailyWeather
                                    )
                            )
                            dailyWeather = mutableListOf()
                            check = 3
                        }
                        else if(timeValue > current+3000 && timeValue <= current + 165600){
                            val roundUp = (timeValue - (timeValue % 3600) + 3600).toString()
                            val jsonDataObject = getWeatherInfo(lat, long)
                            val temp = jsonDataObject.getJSONArray("hourly")
                            val temp1 = jsonDataObject.getJSONArray("daily")

                            for(i in 0 until temp1.length()){
                                val newJson = JSONObject(temp1[i].toString())

                                val weatherCondition1 = JSONObject(
                                        (newJson.getString("weather")).replace(
                                                "[",
                                                ""
                                        ).replace("]", "")
                                ).optString("main")

                                val icon1 = JSONObject(
                                        (newJson.getString("weather")).replace(
                                                "[",
                                                ""
                                        ).replace("]", "")
                                ).optString("icon")


                                val time1 = java.time.format.DateTimeFormatter.ISO_INSTANT
                                        .format(java.time.Instant.ofEpochSecond(newJson.getString("dt").toLong()))

                                val tempKelvinMax1 = JSONObject(newJson.getString("temp")).getString("max").toFloat()

                                val tempFMax1 = ((tempKelvinMax1 - 273.15) * 9/5 + 32).toFloat().roundToInt()

                                val tempKelvinMin1 = JSONObject(newJson.getString("temp")).getString("min").toFloat()

                                val tempFMin1 = ((tempKelvinMin1 - 273.15) * 9/5 + 32).toFloat().roundToInt()

                                dailyWeather.add(DailyWeather(tempFMin1, tempFMax1, weatherCondition1, icon1, time1, locality))
                            }

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
                                                    timeZone,
                                                    dailyWeather
                                            )
                                    )
                                    dailyWeather = mutableListOf()
                                    check = 3
                                    break
                                }
                            }
                        }
                        else if(timeValue > current + 165600 && timeValue <= current + 777600){
                            val jsonDataObject = getWeatherInfo(lat, long)
                            val temp = jsonDataObject.getJSONArray("daily")

                            for(i in 0 until temp.length()){
                                val newJson = JSONObject(temp[i].toString())

                                val weatherCondition1 = JSONObject(
                                        (newJson.getString("weather")).replace(
                                                "[",
                                                ""
                                        ).replace("]", "")
                                ).optString("main")

                                val icon1 = JSONObject(
                                        (newJson.getString("weather")).replace(
                                                "[",
                                                ""
                                        ).replace("]", "")
                                ).optString("icon")


                                val time1 = java.time.format.DateTimeFormatter.ISO_INSTANT
                                        .format(java.time.Instant.ofEpochSecond(newJson.getString("dt").toLong()))

                                val tempKelvinMax1 = JSONObject(newJson.getString("temp")).getString("max").toFloat()

                                val tempFMax1 = ((tempKelvinMax1 - 273.15) * 9/5 + 32).toFloat().roundToInt()

                                val tempKelvinMin1 = JSONObject(newJson.getString("temp")).getString("min").toFloat()

                                val tempFMin1 = ((tempKelvinMax1 - 273.15) * 9/5 + 32).toFloat().roundToInt()

                                dailyWeather.add(DailyWeather(tempFMin1, tempFMax1, weatherCondition1, icon1, time1, locality))
                            }

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

                                    val hour = time.substringAfter("T").substringBefore(":").toInt()

                                    var dayNight = ""

                                    when (hour) {
                                        in 0..4 -> dayNight = "night"
                                        in 5..8 -> dayNight = "morn"
                                        in 9..15 -> dayNight = "day"
                                        in 16..19 -> dayNight = "eve"
                                        in 20..23 -> dayNight = "night"
                                    }

                                    tempKelvin = JSONObject(newJson.getString("temp")).getString(dayNight).toFloat()

                                    tempF = ((tempKelvin - 273.15) * 9/5 + 32).toFloat().roundToInt()
                                    dataList.add(
                                            Data(
                                                    locality,
                                                    tempF,
                                                    weatherCondition,
                                                    icon,
                                                    time,
                                                    timeZone,
                                                    dailyWeather
                                            )
                                    )
                                    dailyWeather = mutableListOf()
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
                            val temp1 = jsonDataObject.getJSONArray("daily")

                            for(i in 0 until temp1.length()){
                                val newJson = JSONObject(temp1[i].toString())

                                val weatherCondition1 = JSONObject(
                                        (newJson.getString("weather")).replace(
                                                "[",
                                                ""
                                        ).replace("]", "")
                                ).optString("main")

                                val icon1 = JSONObject(
                                        (newJson.getString("weather")).replace(
                                                "[",
                                                ""
                                        ).replace("]", "")
                                ).optString("icon")


                                val time1 = java.time.format.DateTimeFormatter.ISO_INSTANT
                                        .format(java.time.Instant.ofEpochSecond(newJson.getString("dt").toLong()))

                                val tempKelvinMax1 = JSONObject(newJson.getString("temp")).getString("max").toFloat()

                                val tempFMax1 = ((tempKelvinMax1 - 273.15) * 9/5 + 32).toFloat().roundToInt()

                                val tempKelvinMin1 = JSONObject(newJson.getString("temp")).getString("min").toFloat()

                                val tempFMin1 = ((tempKelvinMin1 - 273.15) * 9/5 + 32).toFloat().roundToInt()

                                dailyWeather.add(DailyWeather(tempFMin1, tempFMax1, weatherCondition1, icon1, time1, locality))
                            }

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
                                            timeZone,
                                            dailyWeather
                                    )
                            )
                            dailyWeather = mutableListOf()
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

    // This method is empty all the lists that are created earlier to start the computing from beginning.
    private fun clearVariables() {
        location1 = mutableListOf()
        locationLat = mutableListOf()
        locationLong = mutableListOf()
        locality = mutableListOf()
        timeStamp = mutableListOf()
        dataList = mutableListOf()
        dailyWeather = mutableListOf()
    }

    // This method returns the JSON Object of data collected from the mapquest api. This is mainly to find out the travel time between 2 locations.
    private fun getTravelTime(s_lat: Double, s_long: Double, e_lat: Double, e_long: Double): JSONObject {
        return JSONObject(Request("http://www.mapquestapi.com/directions/v2/route?key=A4UUgYYVFNvhyO0HK2vJWPAjjBYHTsGv&from=$s_lat,$s_long&to=$e_lat,$e_long").run())
    }

    //This method returns the JSON Object of data collected from geonames api. This is mainly yo find out weather a location is in USA or not.
    private fun getCountryName(lat: Double, long: Double): JSONObject {
        return JSONObject(Request("http://api.geonames.org/countryCodeJSON?lat=$lat&lng=$long&username=asuglio").run())
    }

    //This method is to return the JSON Object of weather information for a location that the user is interested in.
    private fun getWeatherInfo(lat: Double, long: Double): JSONObject {
        return JSONObject(Request("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$long&appid=09f73a1fbf8932c02e6b56a252ac594f").run())
    }

    /////DEERAJ/////------------------------------------To

    //getting the main_menu using the inflater
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //handling the sign out button on the top right corner
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.signOUT) {

            Log.d("idk", "you definitely clicked me!")

            this.context?.let {

                AuthUI.getInstance().signOut(it).addOnCompleteListener {

                    val intent = Intent(this.context, landing_page::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

}