package com.example.nomadfinal

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.nomadfinal.data.Data
import org.w3c.dom.Text

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels


class WeatherPost: Fragment() {
    // override fun onCreate(savedInstanceState: Bundle?) {
    private val viewM: MainViewModel by activityViewModels()




    // var idk = intent.extras?.getStringArrayList("weatherData")

    //  var newIntent = Intent()

    // newIntent.getExtra()



    //  }

}



//
//
//class WeatherPost : AppCompatActivity()
//{
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.weather_row)
//
//        var idk = intent.extras?.getStringArrayList("weatherData")
//
//
//       // Log.d("weatherDataImport", idk.toString())
//
//
//       // var lol = idk.toString().substring(idk.toString().indexOf("=") + 1, idk.toString().indexOf(","))
//
//      //  var title = findViewById<TextView>(R.id.subRowHeading)
//
//      //  title.text = lol
//
//
//
//
//     //   var idk2 = idk?.toMutableList()
//
//
//
//
//    //    Log.d("haha", "hey")
//
//
//
//
////        if (idk != null)
////        {
////
////            idk.get(0)
////
////
////            title.text = idk.get(0)
////        }
//
//
////        if (idk != null) {
////            Log.d("weatherDataImport2", idk.get(0).toString())
////        }
//
//
//    }
//
//}