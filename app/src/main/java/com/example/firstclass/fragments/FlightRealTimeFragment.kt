package com.example.firstclass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.firstclass.R
import com.example.firstclass.viewmodel.FlightListActivityViewModel
import com.example.firstclass.viewmodel.FlightRealTimeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class FlightRealTimeFragment : Fragment() {

    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var viewModel : FlightRealTimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity() )[FlightRealTimeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flight_real_time, container, false)

        supportMapFragment = childFragmentManager.findFragmentById(R.id.fragment_stats_map_container) as SupportMapFragment
        supportMapFragment.getMapAsync{ map->

            map.mapType= GoogleMap.MAP_TYPE_HYBRID

            viewModel.getFlightRealTimeLiveData().observe(viewLifecycleOwner){

                map.clear()
                map.addMarker( MarkerOptions().position(LatLng(it.states[0][6] as Double , it.states[0][5] as Double  )) )
                var builder = LatLngBounds.builder()
                builder.include(LatLng(it.states[0][6] as Double , it.states[0][5] as Double ))
                var bounds=builder.build()
                var camera= CameraUpdateFactory.newLatLngBounds(bounds,20)
                map.animateCamera(camera)
            }
        }
        return view
    }

    companion object {
        fun newInstance(): FlightRealTimeFragment = FlightRealTimeFragment()
    }
}