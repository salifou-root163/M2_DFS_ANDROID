package com.example.firstclass.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstclass.R
import com.example.firstclass.model.FlightModel
import com.example.firstclass.utils.Utils
import java.util.*

class SingleFlightListAdapter (private val flights : List<FlightModel>

): RecyclerView.Adapter<SingleFlightListAdapter.ViewHolder>(), View.OnClickListener {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.cardView)!!
        val departure = itemView.findViewById<TextView>(R.id.departureCity)!!
        val arrival = itemView.findViewById<TextView>(R.id.arrivalCity)!!
        val arrivalTime = itemView.findViewById<TextView>(R.id.arrivalTime)!!
        val departureTime = itemView.findViewById<TextView>(R.id.departureTime)!!
        val flightDuration = itemView.findViewById<TextView>(R.id.flightDuration)!!


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flight, parent, false)
        return ViewHolder(viewItem);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight=flights[position]
        with(holder){
            cardView.tag=flight
            departure.text=flight.estDepartureAirport
            arrival.text=flight.estArrivalAirport

            var convertDeparturetime: String? =convertLongToTime(flight.firstSeen)
            var convertArrivaltime: String? =convertLongToTime(flight.lastSeen)
            var getFlightduration=flight.lastSeen-flight.firstSeen

            departureTime.text=convertDeparturetime
            arrivalTime.text=convertArrivaltime

            var convertFlightduration: String? = Utils.formatFlightDuration(getFlightduration)
            flightDuration.text=convertFlightduration

        }
    }

    override fun getItemCount(): Int =flights.size
    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    fun convertLongToTime(time: Long): String {
        var myTime:String="%02d:%02d".format(Date(time * 1000).hours, Date(time * 1000).minutes)

        return myTime
    }
}