package com.example.firstclass.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firstclass.R
import com.example.firstclass.activity.FlightRealTimePositionActivity
import com.example.firstclass.activity.SingleFLightListActivity
import com.example.firstclass.viewmodel.FlightListActivityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import java.text.DecimalFormat


class FlightMapFragment : Fragment() {
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var viewModel : FlightListActivityViewModel
    private lateinit var realTimeBtn :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity() )[FlightListActivityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flight_map, container, false)
        supportMapFragment = childFragmentManager.findFragmentById(R.id.fragment_map_container) as SupportMapFragment

        realTimeBtn= view.findViewById(R.id.realTime)
        realTimeBtn.isEnabled=false

        //get data from flightMapActivity to set flights list button if we are on phone screen
        val bundle = arguments
        if (bundle != null){
            val url = bundle!!.getString("url")
            val icao = bundle!!.getString("icao24")
            view.findViewById<View>(R.id.showListFligths).setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    when (v.id) {
                        R.id.showListFligths -> {
                            val intent = Intent(activity, SingleFLightListActivity::class.java)
                            intent.putExtra("url", url )
                            intent.putExtra("icao24", icao )
                            startActivity(intent) //Edited here
                        }
                    }
                }
            })
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMarkersForFlight()
    }

    //function to refresh map fragment in case of tablet screen
    fun showMarkersForFlight(){

        supportMapFragment.getMapAsync{ map->
            map.mapType=GoogleMap.MAP_TYPE_HYBRID
            //val viewModel = ViewModelProvider(requireActivity()).get(FlightListActivityViewModel::class.java)
            viewModel.getClickedFlightLiveData().observe(viewLifecycleOwner){

                var url= "https://opensky-network.org/api/tracks/all?icao24=" + it.icao24 + "&time=" + it.lastSeen
                viewModel.getFlightMarkers(url)

            }

            //function to show map, markers and markers click event listener
            map.setOnMapLoadedCallback(OnMapLoadedCallback {

                var builder = LatLngBounds.builder()
                viewModel.getFlightMarkersLiveData().observe(viewLifecycleOwner){

                    map.clear()
                    //adding markers on map
                    for (i in it.path){
                        map.addMarker( MarkerOptions().position(LatLng(i[1] as Double, i[2] as Double )) )
                        builder.include(LatLng(i[1] as Double, i[2] as Double ))
                    }
                    //move map cam on added markers
                    var bounds=builder.build()
                    var camera=CameraUpdateFactory.newLatLngBounds(bounds,20)
                    map.animateCamera(camera)

                    //setting markers click listener
                    map.setOnMarkerClickListener(OnMarkerClickListener { marker ->

                        for (i in it.path){

                            val pos = "lat/lng: ("+i[1]+","+i[2]+")"

                            if ( pos == marker.position.toString() ){
                                val time = i[0]
                                //code to prevent scientific casting of time
                                    val df = DecimalFormat("#")
                                    df.setMaximumFractionDigits(0)
                                    val time2=df.format(time)
                                //end
                                val url="https://opensky-network.org/api/states/all?icao24="+it.icao24+"&time="+time2

                                realTimeBtn.isEnabled=true
                                realTimeBtn.setOnClickListener { view ->
                                    val intent = Intent(activity, FlightRealTimePositionActivity::class.java)
                                    intent.putExtra("url", url  )
                                    startActivity(intent )
                                }
                            }
                        }
                        false
                    })
                }

            })
        }
    }

    companion object {
        fun newInstance(): FlightMapFragment = FlightMapFragment()
    }
}