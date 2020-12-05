package com.example.nomadfinal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.nomadfinal.data.Data
import kotlin.collections.HashMap

 class WeatherListAdapter(private val viewModel: MainViewModel) : RecyclerView.Adapter<WeatherListAdapter.VH>()
{
    // Using hashmap to hold the drawables for a particular icon name.
    // This makes it easier to grab the correct drawable.
    private var hashMap:HashMap<String, Int> = HashMap()

    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        private var subHead: TextView = itemView.findViewById(R.id.subRowHeading)

        var subDescription: TextView = itemView.findViewById(R.id.subRowDetails)

        private var time: TextView =  itemView.findViewById(R.id.time)

        private var timeZone: TextView =  itemView.findViewById(R.id.timeZone)

        private var subPic: ImageView = itemView.findViewById(R.id.subRowPic)

        private var weather: TextView = itemView.findViewById(R.id.weather)


        init
        {
            Log.d("SOS", "are you reading this")

            hashMap.put("01d", R.drawable.one_day)
            hashMap.put("01n", R.drawable.one_night)
            hashMap.put("02d", R.drawable.two_day)
            hashMap.put("02n", R.drawable.two_night)
            hashMap.put("03d", R.drawable.three_day)
            hashMap.put("03n", R.drawable.three_night)
            hashMap.put("04d", R.drawable.four_day)
            hashMap.put("04n", R.drawable.four_night)
            hashMap.put("09d", R.drawable.nine_day)
            hashMap.put("09n", R.drawable.nine_night)
            hashMap.put("10d", R.drawable.ten_day)
            hashMap.put("10n", R.drawable.ten_night)
            hashMap.put("11d", R.drawable.eleven_day)
            hashMap.put("11n", R.drawable.eleven_night)
            hashMap.put("13d", R.drawable.thirteen_day)
            hashMap.put("13n", R.drawable.thirteen_night)
            hashMap.put("50d", R.drawable.fifty_day)
            hashMap.put("50n", R.drawable.fifty_night)


            // OnClick Listener for each element in the Weather List to show the
            // 10-day Weather forecast of the location that was clicked.
            itemView.setOnClickListener{

                //Observing the LivaData of dailyWeather for particular location that was clicked
                val weatherNewData = viewModel.observeWeather().value?.get(adapterPosition)?.dailyWeather

                // posting the data of current location to the dailyWeatherData to observe it later while showing to the user.
                viewModel.dailyWeatherData.postValue(weatherNewData)

                // Moving to the DailyWeatherList Fragment to handle the 10-day Weather forecast
                (itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, DailyWeatherList())
                    .addToBackStack(null)
                    .commit()

            }
        }

        fun bind(item: Data)
        {
            subHead.text = "Locality: " + item.location

            val F = "\u2109"
            val tempString = item.temperature.toString()
            val totalString = tempString + F

            subDescription.text = totalString


            hashMap[item.icon]?.let { subPic.setImageResource(it) }

            val date = item.time.substringBefore("T")

            var timeD = item.time.substringAfter("T").substringBefore("Z").dropLast(3)

            val ampM = timeD

            when(ampM.dropLast(3).toInt()){
                0 -> timeD = "12" + timeD.drop(2) + " AM"
                12 -> timeD = "$timeD PM"
                in 1..11 -> timeD += " AM"
                else -> {
                    val ab = timeD
                    val bc = (ab.dropLast(3).toInt() -12).toString()
                    timeD = bc + timeD.drop(2) + " PM"
                }
            }

            time.text = "on $date at $timeD"

            timeZone.text = "Time Zone: " + item.timeZone

            weather.text = item.weather

        }
    }

    //on create view holder method...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    {
        //layout inflater
        val v = LayoutInflater.from(parent.context).inflate(R.layout.weather_row, parent, false)
        return VH(v)
    }

   //on bind view holder method...
    override fun onBindViewHolder(holder: VH, position: Int)
    {
        viewModel.observeWeather().value?.get(position)?.let{ holder.bind(it) }
    }

    //get item count method...
   override fun getItemCount(): Int
    {
        return viewModel.observeWeather().value!!.size
    }
}

