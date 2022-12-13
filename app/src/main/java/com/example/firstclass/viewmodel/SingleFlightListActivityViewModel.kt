package com.example.firstclass.viewmodel

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

class SingleFlightListActivityViewModel : ViewModel() {

    private val SingleFlightsListLiveData = MutableLiveData<List<FlightModel>>(ArrayList())

    fun setSingleFlightsListLiveData( data:List<FlightModel>? ){
        SingleFlightsListLiveData.value=data
    }

    fun getSingleFlightsListLiveData(): MutableLiveData<List<FlightModel>> {
        return SingleFlightsListLiveData
    }

    fun getSingleFlights(url:String){
        viewModelScope.launch {

            var res= withContext(Dispatchers.IO){
                val res = HashMap<String, String>()
                RequestManager.getSuspended(url, res)
            }
            val fly = object : TypeToken<List<FlightModel>>() {}.type
            val flights = Gson().fromJson<List<FlightModel>>(res, fly)
            if (flights != null){
                setSingleFlightsListLiveData(flights)
            }else{
                setSingleFlightsListLiveData(java.util.ArrayList())
            }
        }
    }

}