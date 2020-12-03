package com.example.nomadfinal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.nomadfinal.data.DailyWeather
import com.example.nomadfinal.data.Data
import kotlin.collections.HashMap

class DailyWeatherListAdapter(private val viewModel: MainViewModel) : RecyclerView.Adapter<DailyWeatherListAdapter.VH>()
{

    private var hashMap:HashMap<String, Int> = HashMap()

    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        //description
        var subDescription: TextView = itemView.findViewById(R.id.subRowDetailsDaily)

        private var time: TextView =  itemView.findViewById(R.id.timeDaily)

        //sub picture
        private var subPic: ImageView = itemView.findViewById(R.id.subRowPicDaily)

        private var weather: TextView = itemView.findViewById(R.id.weatherDaily)


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

        }

        fun bind(item: DailyWeather)
        {

            var F = "\u2109"
            var tempStringMin = item.minTemp.toString()

            var tempStringMax = item.maxTemp.toString()

            subDescription.text = "High: $tempStringMax$F Low: $tempStringMin$F"

            hashMap[item.icon]?.let { subPic.setImageResource(it) }

            time.text = item.time.substringBefore("T")

            weather.text = item.weather


        }
    }

    //on create view holder method...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    {
        //layout inflater
        val v = LayoutInflater.from(parent.context).inflate(R.layout.daily_weather_row, parent, false)
        return VH(v)
    }

    //on bind view holder method...
    override fun onBindViewHolder(holder: VH, position: Int)
    {
        viewModel.observeDailyWeatherData().value?.get(position)?.let{ holder.bind(it) }
    }

    //get item count method...
    override fun getItemCount(): Int
    {
        return viewModel.observeDailyWeatherData().value!!.size
    }
}

