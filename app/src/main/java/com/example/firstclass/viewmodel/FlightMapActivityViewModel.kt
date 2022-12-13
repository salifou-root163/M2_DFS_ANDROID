package com.example.firstclass.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstclass.model.FlightMarkersModel
import com.example.firstclass.utils.RequestManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlightMapActivityViewModel: ViewModel() {

    private val FlightMarkersLiveData = MutableLiveData<FlightMarkersModel>()

    fun setFlightMarkersLiveData( data:FlightMarkersModel? ){
        FlightMarkersLiveData.value=data
    }

    fun getFlightMarkersLiveData(): MutableLiveData<FlightMarkersModel> {
        return FlightMarkersLiveData
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