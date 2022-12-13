package com.example.firstclass.model

import java.util.*
import kotlin.collections.ArrayList



    data class  FlightMarkersModel (val icao24: String,
                                   val callsign: String,
                                   val startTime: Long,
                                   val endTime: Long,
                                   val path: ArrayList<List<Any>>,
    )






