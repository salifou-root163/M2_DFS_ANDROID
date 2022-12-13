package com.example.firstclass.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.firstclass.R
import com.example.firstclass.fragments.FlightListFragment
import com.example.firstclass.fragments.SingleFlightListFragment
import com.example.firstclass.viewmodel.FlightListActivityViewModel
import com.example.firstclass.viewmodel.SingleFlightListActivityViewModel

class SingleFLightListActivity : AppCompatActivity() {
    private lateinit var viewModel: SingleFlightListActivityViewModel
    private lateinit var singleflightListFragment: SingleFlightListFragment
    private lateinit var icaoTxt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_flight_list)
        singleflightListFragment = supportFragmentManager.findFragmentById(R.id.single_flight_list_fragment) as SingleFlightListFragment
        viewModel = ViewModelProvider(this).get(SingleFlightListActivityViewModel::class.java)

        val url = intent.getStringExtra("url")

        icaoTxt = findViewById(R.id.icaoTxt)
        icaoTxt.text=intent.getStringExtra("icao24")

        if (url != null) {
            viewModel.getSingleFlights(url)
        }
    }
}