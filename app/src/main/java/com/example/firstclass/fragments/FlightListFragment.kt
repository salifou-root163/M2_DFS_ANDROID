package com.example.firstclass.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstclass.*
import com.example.firstclass.activity.FlightListActivity
import com.example.firstclass.adapter.FlightListAdapter
import com.example.firstclass.model.FlightModel
import com.example.firstclass.viewmodel.FlightListActivityViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class FlightListFragment : Fragment(), FlightListAdapter.FlightItemListener {

    interface FlightFragmentListener {
        fun onFlightSelected(flightModel: FlightModel)
    }

    var listener:FlightFragmentListener? = null

    private lateinit var flights: MutableLiveData<List<FlightModel>>
    private lateinit var recylcerView: RecyclerView
    private lateinit var adapter: FlightListAdapter
    private lateinit var viewModel: FlightListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity() as FlightListActivity)[FlightListActivityViewModel::class.java]
        flights = viewModel.getFlightsListLiveData();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flight_list, container, false)
        recylcerView = view.findViewById(R.id.flights_recycler_list)
        recylcerView.layoutManager=LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        flights.observe(viewLifecycleOwner){
            adapter = FlightListAdapter(it, this)
            recylcerView.adapter=adapter
        }

    }

    override fun onFlightSelected(flightModel: FlightModel) {
        listener?.onFlightSelected(flightModel)
    }




}