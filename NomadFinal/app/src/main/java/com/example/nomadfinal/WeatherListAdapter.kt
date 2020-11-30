package com.example.nomadfinal


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

import com.example.nomadfinal.data.Data


// NB: Could probably unify with PostRowAdapter if we had two
// different VH and override getItemViewType
// https://medium.com/@droidbyme/android-recyclerview-with-multiple-view-type-multiple-view-holder-af798458763b
 class WeatherListAdapter(private val viewModel: MainViewModel) : RecyclerView.Adapter<WeatherListAdapter.VH>()
{

    //instantiate test... something I tried earlier
    //var idk = Subreddits()

    private var hashMap:HashMap<String,Int> = HashMap()

    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // var idk = findViewById<>
        // XXX Write me.
        // NB: This one-liner will exit the current fragment

        //dont use this yet...
        // var notSure = (itemView.context as FragmentActivity).supportFragmentManager.popBackStack()

        //heading
        var subHead = itemView.findViewById<TextView>(R.id.subRowHeading)


        //description
        var subDescription = itemView.findViewById<TextView>(R.id.subRowDetails)

        //sub picture
        var subPic = itemView.findViewById<ImageView>(R.id.subRowPic)


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

        fun bind(item: Data)
        {


            subHead.text = item.location

            subDescription.text = item.temperature.toString()

            hashMap[item.icon]?.let { subPic.setImageResource(it) }

        }
    }

    //on create view holder method...
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
    {
        //layout inflator
        val v = LayoutInflater.from(parent.context).inflate(R.layout.weather_row, parent, false)
        return VH(v)
    }
//
//    //on bind view holder method...
    override fun onBindViewHolder(holder: VH, position: Int)
    {
        //on bind view utilize our observerMediatorLiveData
        viewModel.observeWeather().value?.get(position)?.let{ holder.bind(it) }
    }
//
//    //get item count method...
   override fun getItemCount(): Int
    {
        //on get item utilize our observermediatorLiveDataSubreddit
        return viewModel.observeWeather().value!!.size
    }
}

