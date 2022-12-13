package com.example.firstclass.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.firstclass.R
import com.example.firstclass.fragments.FlightMapFragment
import com.example.firstclass.viewmodel.FlightListActivityViewModel

class FlightMapActivity : AppCompatActivity() {
    private lateinit var viewModel: FlightListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_map)

        viewModel = ViewModelProvider(this).get(FlightListActivityViewModel::class.java)

        var icao= intent.getStringExtra("icao24")
        var lastSeen= intent.getStringExtra("lastSeen")
        var url= "https://opensky-network.org/api/tracks/all?icao24=" + icao + "&time=" + lastSeen

        //execute query to show all markers
        viewModel.getFlightMarkers(url)

        // sending url var to fragment en replace activity framelayout by map fragment
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val myFragment = FlightMapFragment()

        val b = Bundle()
        b.putString("url",intent.getStringExtra("url")  )
        b.putString("icao24",icao )

        myFragment.arguments=b
        fragmentTransaction.add(R.id.frame_layout, myFragment).commit();











    }


}