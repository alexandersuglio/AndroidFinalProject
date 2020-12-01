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


class DailyWeatherList: Fragment() {
    private val viewModel1: MainViewModel by activityViewModels()

    companion object
    {
        fun newInstance(): DailyWeatherList {
            return DailyWeatherList()
        }
    }


    fun initSwipeLayout(root: View)
    {
        var swipe = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        setHasOptionsMenu(true);

        //then return view....
        val view = inflater.inflate(R.layout.fragment_rv, container, false)

        //Recycler View
        val RV = view.findViewById<RecyclerView>(R.id.recyclerView)

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