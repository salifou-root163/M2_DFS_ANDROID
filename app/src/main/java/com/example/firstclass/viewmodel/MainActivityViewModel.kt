package com.example.firstclass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firstclass.model.Airport
import com.example.firstclass.utils.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivityViewModel : ViewModel() {

    private val beginDateLiveData = MutableLiveData<Calendar>(Calendar.getInstance())
    private val endDateLiveData = MutableLiveData<Calendar>(Calendar.getInstance())


    private val bgnDate=MutableLiveData<Long>()
    private val endDate=MutableLiveData<Long>()
    fun getBeginDateLive(): LiveData<Long> {
        return bgnDate
    }
    fun getEndDateLive(): LiveData<Long> {
        return endDate
    }
    /*
    fun updateCalendarLiveData(dateType: DateType, calendar: Calendar){
        if(dateType == DateType.BEGIN) beginDateLiveData.value = calendar else endDateLiveData.value = calendar
    }
     */


    private val airportListLiveData = MutableLiveData<List<Airport>>()
    private val airportListNamesLiveData = MutableLiveData<List<String>>()

    init{
        val airportList = Utils.generateAirportList()
        airportListLiveData.value = airportList
        val airportNamesList = ArrayList<String>()
        //Populate the list of airport names
        for (airport in airportList) {
            airportNamesList.add(airport.getFormattedName())
        }
        airportListNamesLiveData.value = airportNamesList
    }

    enum class DateType {
        BEGIN, END
    }

    fun getBeginDateLiveData(): LiveData<Calendar> {
        return beginDateLiveData
    }

    fun getEndDateLiveData(): LiveData<Calendar>{
        return endDateLiveData
    }

    fun updateCalendarLiveData(dateType: DateType, calendar: Calendar){
        if(dateType == DateType.BEGIN) beginDateLiveData.value = calendar else endDateLiveData.value = calendar
    }

    fun getAirportNamesListLiveData():LiveData<List<String>>{
        return airportListNamesLiveData
    }

    fun getAirportListLiveData():LiveData<List<Airport>>{
        return airportListLiveData
    }




}