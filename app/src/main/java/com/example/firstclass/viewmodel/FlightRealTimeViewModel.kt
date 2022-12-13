package com.example.firstclass.viewmodel;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstclass.model.FlightMarkersModel
import com.example.firstclass.model.FlightRealTimeModel
import com.example.firstclass.utils.RequestManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

 class FlightRealTimeViewModel:ViewModel() {

    private val FlightRealTimeLiveData = MutableLiveData<FlightRealTimeModel>()

    fun setFlightRealTimeLiveData( data:FlightRealTimeModel? ){
        FlightRealTimeLiveData.value=data
    }

    fun getFlightRealTimeLiveData():MutableLiveData<FlightRealTimeModel> {
        return FlightRealTimeLiveData
    }


    fun getFlightStats(url:String){
        viewModelScope.launch {

            var res= withContext(Dispatchers.IO){
                val res = HashMap<String, String>()
                RequestManager.getSuspended(url, res)
            }

            val fly = object : TypeToken<FlightRealTimeModel>() {}.type
            val markers = Gson().fromJson<FlightRealTimeModel>(res, fly)

            if (markers != null){
                setFlightRealTimeLiveData(markers)
            }else{
                setFlightRealTimeLiveData(null)
            }
        }
    }
}
