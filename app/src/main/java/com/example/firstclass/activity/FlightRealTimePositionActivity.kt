package com.example.firstclass.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.firstclass.R
import com.example.firstclass.fragments.FlightMapFragment
import com.example.firstclass.fragments.FlightRealTimeFragment
import com.example.firstclass.viewmodel.FlightListActivityViewModel
import com.example.firstclass.viewmodel.FlightRealTimeViewModel

class FlightRealTimePositionActivity : AppCompatActivity() {

    private lateinit var viewModel: FlightRealTimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_real_time_position)

        viewModel = ViewModelProvider(this).get(FlightRealTimeViewModel::class.java)

        var url= intent.getStringExtra("url")
        if (url != null) {
            viewModel.getFlightStats(url)
        }
        supportFragmentManager.beginTransaction().replace(android.R.id.content, FlightRealTimeFragment.newInstance()).commit()
    }


}