package com.example.firstclass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstclass.R
import com.example.firstclass.activity.FlightListActivity
import com.example.firstclass.activity.SingleFLightListActivity
import com.example.firstclass.adapter.FlightListAdapter
import com.example.firstclass.adapter.SingleFlightListAdapter
import com.example.firstclass.model.FlightModel
import com.example.firstclass.viewmodel.FlightListActivityViewModel
import com.example.firstclass.viewmodel.SingleFlightListActivityViewModel


class SingleFlightListFragment : Fragment() {

    private lateinit var flights: MutableLiveData<List<FlightModel>>
    private lateinit var recylcerView: RecyclerView
    private lateinit var adapter: SingleFlightListAdapter
    private lateinit var viewModel: SingleFlightListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity() as SingleFLightListActivity)[SingleFlightListActivityViewModel::class.java]
        flights = viewModel.getSingleFlightsListLiveData();

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_flight_list, container, false)
        recylcerView = view.findViewById(R.id.single_flights_recycler_list)
        recylcerView.layoutManager= LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flights.observe(viewLifecycleOwner){
            adapter = SingleFlightListAdapter(it)
            recylcerView.adapter=adapter
        }
    }


}