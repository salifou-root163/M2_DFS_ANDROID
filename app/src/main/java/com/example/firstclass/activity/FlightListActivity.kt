package com.example.firstclass.activity

import android.content.Intent
import android.net.Uri
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceInfo.newInstance
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firstclass.fragments.FlightMapFragment

import com.example.firstclass.R
import com.example.firstclass.fragments.FlightListFragment
import com.example.firstclass.model.FlightModel

import com.example.firstclass.viewmodel.FlightListActivityViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat



class FlightListActivity : AppCompatActivity(), FlightListFragment.FlightFragmentListener {
    private lateinit var viewModel: FlightListActivityViewModel
    private lateinit var flightListFragment: FlightListFragment
    private lateinit var showFlightList:Button
    private var flightMapFragment: FlightMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        flightListFragment = supportFragmentManager.findFragmentById(R.id.flight_list_fragment) as FlightListFragment
        flightListFragment.listener=this
        flightMapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map_container) as FlightMapFragment?

        viewModel = ViewModelProvider(this).get(FlightListActivityViewModel::class.java)
        val url = intent.getStringExtra("url")

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        if (url != null) {
            progressBar.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.Main) {
                viewModel.getFlights(url)


                progressBar.visibility = View.GONE
            }
        }
    }

    //Handle flight selecting
    override fun onFlightSelected(flightModel: FlightModel) {
        val isPhone = findViewById<FragmentContainerView>(R.id.fragment_map_container) == null
        val beginDate = intent.getStringExtra("begin")
        val endDate = intent.getStringExtra("end")

        Log.d("iciiiiiiiiiiiiiiiii",beginDate.toString());


        var url="https://opensky-network.org/api/flights/aircraft?icao24="+flightModel.icao24+"&begin="+beginDate+"&end="+endDate;

        viewModel.setClickedFlightLiveData(flightModel)

        // if we are on screen phone ; start new activity else refresh map container (fragment)
        if (isPhone) {
            val intent = Intent(this, FlightMapActivity::class.java)
            intent.putExtra("icao24",flightModel.icao24.toString())
            intent.putExtra("lastSeen",flightModel.lastSeen.toString())
            intent.putExtra("url", url.toString()  )
            startActivity(intent )
        }else{
            //refresh map fragment end show markers function
            flightMapFragment?.showMarkersForFlight()

            //setting button to show selected airplan flights
            showFlightList = findViewById<Button>(R.id.showListFligths)

            showFlightList.setOnClickListener{
                val i = Intent(this, SingleFLightListActivity::class.java)
                i.putExtra("icao24",flightModel.icao24.toString())
                i.putExtra("url", url.toString()   )
                startActivity(i)
            }

        }
    }



}