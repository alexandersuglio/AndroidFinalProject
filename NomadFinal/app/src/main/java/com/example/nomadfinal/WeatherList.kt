package com.example.nomadfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.weather_row.*


class WeatherList: Fragment() {
        private val viewM: MainViewModel by activityViewModels()

    companion object
    {
        fun newInstance(): WeatherList {
            return WeatherList()
        }
    }


    fun initSwipeLayout(root: View)
    {
        var swipe = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        setHasOptionsMenu(true);


        //then return view....
        var view = inflater.inflate(R.layout.fragment_rv, container, false)

        //Recycler View
        var RV = view.findViewById<RecyclerView>(R.id.recyclerView)


        //adapter
        var adapter = WeatherListAdapter(viewM)

        //adapter hook up
        RV.adapter = adapter
        RV.layoutManager = LinearLayoutManager(context)

        //observer
        viewM.observeWeather().observe(viewLifecycleOwner,
                {
                    adapter.notifyDataSetChanged()
                })

        //call swipe
        initSwipeLayout(view)

        //view
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)


    }

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

