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


        }

        fun bind(item: Data)
        {


            subHead.text = item.location

            subDescription.text = item.temperature.toString()

            subPic.setImageResource(R.drawable.roadtrip2)

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

