/*package com.example.firstclass

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by sergio on 07/11/2021
 * All rights reserved GoodBarber
 */
data class FlightModel(
    val icao24: String?,
    val firstSeen: Long,
    val estDepartureAirport: String?,
    val lastSeen: Long,
    val estArrivalAirport: String?,
    val callsign: String?,
    val estDepartureAirportHorizDistance: Int,
    val estDepartureAirportVertDistance: Int,
    val estArrivalAirportHorizDistance: Int,
    val estArrivalAirportVertDistance: Int,
    val departureAirportCandidatesCount: Int,
    val arrivalAirportCandidatesCount: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<FlightModel> {
        override fun createFromParcel(parcel: Parcel): FlightModel {
            return FlightModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightModel?> {
            return arrayOfNulls(size)
        }
    }
}*/

package com.example.firstclass.model

/**
 * Created by sergio on 07/11/2021
 * All rights reserved GoodBarber
 */
data class FlightModel (val icao24: String,
                        val firstSeen: Long,
                        val estDepartureAirport: String,
                        val lastSeen: Long,
                        val estArrivalAirport: String,
                        val callsign: String,
                        val estDepartureAirportHorizDistance: Int,
                        val estDepartureAirportVertDistance: Int,
                        val estArrivalAirportHorizDistance: Int,
                        val estArrivalAirportVertDistance: Int,
                        val departureAirportCandidatesCount: Int,
                        val arrivalAirportCandidatesCount: Int)

