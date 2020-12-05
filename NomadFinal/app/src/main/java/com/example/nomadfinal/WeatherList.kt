package com.example.nomadfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firebase.ui.auth.AuthUI


class WeatherList: Fragment() {

    //Using val viewMto observe the Live data of weatherInfo from the MainViewModel
    private val viewM: MainViewModel by activityViewModels()

    //As we do not require swipe refresh, we are setting the refresh listener to false
    private fun initSwipeLayout(root: View)
    {
        val swipe = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        //Setting the option menu to true to make the Sign out button visible
        setHasOptionsMenu(true)

        //Inflating the layout from created fragment_rv
        val view = inflater.inflate(R.layout.fragment_rv, container, false)

        // Getting the Recycler View for showing the Weather for every location
        val RV = view.findViewById<RecyclerView>(R.id.recyclerView)


        //passing the viewM to Adapter created specifically for handling the Weather information for every location.
        val adapter = WeatherListAdapter(viewM)

        //adapter hook up
        RV.adapter = adapter
        RV.layoutManager = LinearLayoutManager(context)

        //Hooking up the adapter and getting the layout manager for the current context
        viewM.observeWeather().observe(viewLifecycleOwner,
                {
                    adapter.notifyDataSetChanged()
                })

        //call swipe function to handle the Swipe Refresh
        initSwipeLayout(view)

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

