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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firebase.ui.auth.AuthUI


class DailyWeatherList: Fragment() {
    private val viewModel1: MainViewModel by activityViewModels()

    companion object
    {
        fun newInstance(): DailyWeatherList {
            return DailyWeatherList()
        }
    }


//    fun initSwipeLayout(root: View)
//    {
//        var swipe = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayoutNew)
//
//        swipe.setOnRefreshListener {
//            swipe.isRefreshing = false
//        }
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //fragmentManager?.apply()
        getParentFragmentManager().apply {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        //inflater.inflate(R.layout.activity_splash_page,container, false)

        setHasOptionsMenu(true);

        //then return view....
        val view = inflater.inflate(R.layout.daily_fragment, container, false)


        var heading = view.findViewById<TextView>(R.id.headingNew)

        heading.text = "10 Day Weather Forecast For: " + viewModel1.observeDailyWeatherData().value?.get(0)?.location


        //Recycler View
        val RV = view.findViewById<RecyclerView>(R.id.recyclerViewNew)

        //adapter
        val adapter = DailyWeatherListAdapter(viewModel1)

        //adapter hook up
        RV.adapter = adapter
        RV.layoutManager = LinearLayoutManager(context)

        //observer
        viewModel1.observeDailyWeatherData().observe(viewLifecycleOwner,
            {
                adapter.notifyDataSetChanged()
            })

        //call swipe
      //  initSwipeLayout(view)

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