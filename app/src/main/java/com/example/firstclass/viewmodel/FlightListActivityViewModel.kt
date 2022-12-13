package com.example.firstclass.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstclass.model.FlightMarkersModel
import com.example.firstclass.model.FlightModel
import com.example.firstclass.utils.RequestManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FlightListActivityViewModel : ViewModel() {

    private val FlightsListLiveData = MutableLiveData<List<FlightModel>>(ArrayList())
    private val clickedFlightLiveData = MutableLiveData<FlightModel>()
    private val FlightMarkersLiveData = MutableLiveData<FlightMarkersModel>()

    fun setFlightsListLiveData( data:List<FlightModel>? ){
        FlightsListLiveData.value=data
    }

    fun getFlightsListLiveData(): MutableLiveData<List<FlightModel>> {
        return FlightsListLiveData
    }

    fun setFlightMarkersLiveData( data:FlightMarkersModel? ){
        FlightMarkersLiveData.value=data
    }

    fun getFlightMarkersLiveData(): MutableLiveData<FlightMarkersModel> {
        return FlightMarkersLiveData
    }

    fun getClickedFlightLiveData(): LiveData<FlightModel> {
        return clickedFlightLiveData
    }

    fun setClickedFlightLiveData(flight: FlightModel) {
        clickedFlightLiveData.value = flight
    }

    fun getFlights(url:String){
        viewModelScope.launch {

            var res= withContext(Dispatchers.IO){
                val res = HashMap<String, String>()
                RequestManager.getSuspended(url, res)
            }
            val fly = object : TypeToken<List<FlightModel>>() {}.type
            val flights = Gson().fromJson<List<FlightModel>>(res, fly)
            if (flights != null){
                setFlightsListLiveData(flights)
            }else{
                setFlightsListLiveData(java.util.ArrayList())
            }
        }
    }

    fun getFlightMarkers(url:String){
        viewModelScope.launch {

            var res= withContext(Dispatchers.IO){
                val res = HashMap<String, String>()
                RequestManager.getSuspended(url, res)
            }
            val fly = object : TypeToken<FlightMarkersModel>() {}.type
            val markers = Gson().fromJson<FlightMarkersModel>(res, fly)

            if (markers != null){
                setFlightMarkersLiveData(markers)

            }else{
                setFlightMarkersLiveData(null)
            }
        }

    }
}