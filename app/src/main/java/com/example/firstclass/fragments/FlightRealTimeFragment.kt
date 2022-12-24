package com.example.firstclass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

        val real_icao = view.findViewById<TextView>(R.id.real_icao)
        val real_callsign = view.findViewById<TextView>(R.id.real_callsign)
        val real_origin_country = view.findViewById<TextView>(R.id.real_origin_country)
        val real_time_position = view.findViewById<TextView>(R.id.real_time_position)
        val real_time_last_contact = view.findViewById<TextView>(R.id.real_time_last_contact)
        val real_time_longitude = view.findViewById<TextView>(R.id.real_time_longitude)
        val real_time_latitude = view.findViewById<TextView>(R.id.real_time_latitude)
        val real_time_baro_altitude = view.findViewById<TextView>(R.id.real_time_baro_altitude)

        supportMapFragment = childFragmentManager.findFragmentById(R.id.fragment_stats_map_container) as SupportMapFragment
        supportMapFragment.getMapAsync{ map->

            map.mapType= GoogleMap.MAP_TYPE_HYBRID

            viewModel.getFlightRealTimeLiveData().observe(viewLifecycleOwner){

                map.clear()

                Log.d("test --- test", it.toString());



                real_icao.visibility= View.INVISIBLE
                real_callsign.visibility= View.INVISIBLE
                real_origin_country.visibility= View.INVISIBLE
                real_time_position.visibility= View.INVISIBLE
                real_time_last_contact.visibility= View.INVISIBLE
                real_time_last_contact.visibility= View.INVISIBLE
                real_time_latitude.visibility= View.INVISIBLE
                real_time_baro_altitude.visibility= View.INVISIBLE

                if (it.states!==null){
                    map.addMarker( MarkerOptions().position(LatLng(it.states[0][6] as Double , it.states[0][5] as Double  )) )
                    var builder = LatLngBounds.builder()
                    builder.include(LatLng(it.states[0][6] as Double , it.states[0][5] as Double ))
                    var bounds=builder.build()
                    var camera= CameraUpdateFactory.newLatLngBounds(bounds,20)
                    map.animateCamera(camera)

                    //
                     real_icao.visibility= View.VISIBLE
                     real_callsign.visibility= View.VISIBLE
                     real_origin_country.visibility= View.VISIBLE
                    real_time_position.visibility= View.INVISIBLE
                     real_time_last_contact.visibility= View.VISIBLE
                     real_time_last_contact.visibility= View.VISIBLE
                     real_time_latitude.visibility= View.VISIBLE
                     real_time_baro_altitude.visibility= View.VISIBLE


                    // Set the text of the TextView
                    real_icao.text = it.states[0][0].toString()
                    real_callsign.text = it.states[0][1].toString()
                    real_origin_country.text = it.states[0][2].toString()
                    real_time_position.text = it.states[0][3].toString()
                    real_time_last_contact.text = it.states[0][4].toString()
                    real_time_longitude.text = it.states[0][5].toString()
                    real_time_latitude.text = it.states[0][6].toString()
                    real_time_baro_altitude.text = it.states[0][7].toString()

                }else{
                    val toast = Toast.makeText(context, "Nothing to show.", Toast.LENGTH_SHORT)
                    toast.show()

                }






            }
        }
        return view
    }

    companion object {
        fun newInstance(): FlightRealTimeFragment = FlightRealTimeFragment()
    }
}