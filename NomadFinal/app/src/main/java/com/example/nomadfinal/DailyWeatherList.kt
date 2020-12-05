package com.example.nomadfinal


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI


class DailyWeatherList: Fragment() {

   //Using val viewModel1 to observe the Live data of DailyWeather from the MainViewModel

    private val viewModel1: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        //Setting the option menu to true to make the Sign out button visible
        setHasOptionsMenu(true)

        //Inflating the layout from created daily_fragment
        val view = inflater.inflate(R.layout.daily_fragment, container, false)


        val heading = view.findViewById<TextView>(R.id.headingNew)

        heading.text = "10 Day Weather Forecast For: " + viewModel1.observeDailyWeatherData().value?.get(0)?.location


        // Getting the Recycler View for showing the 10-day Daily Weather
        val RV = view.findViewById<RecyclerView>(R.id.recyclerViewNew)

        //passing the viewModel1 to Adapter created specifically for handling the 10-day daily weather forecast.
        val adapter = DailyWeatherListAdapter(viewModel1)

        //Hooking up the adapter and getting the layout manager for the current context
        RV.adapter = adapter
        RV.layoutManager = LinearLayoutManager(context)

        //observing the Live Data by using the method created in the MainViewModel
        viewModel1.observeDailyWeatherData().observe(viewLifecycleOwner,
            {
                adapter.notifyDataSetChanged()
            })

        //returning the final view
        return view
    }

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