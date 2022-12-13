package com.example.firstclass.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.firstclass.R
import com.example.firstclass.viewmodel.MainActivityViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

import com.example.firstclass.utils.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var beginDateLabel: TextView
    private lateinit var endDateLabel: TextView
    private lateinit var beginDateCal: ImageView
    private lateinit var endDateCal: ImageView
    private lateinit var flyType : Switch
    private lateinit var search : Button

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        beginDateLabel = findViewById<TextView>(R.id.beginDate)
        endDateLabel = findViewById<TextView>(R.id.endDate)
        beginDateCal = findViewById(R.id.beginDateCalendar);
        endDateCal = findViewById(R.id.endDateCalendar);
        flyType = findViewById(R.id.switchBtn)
        search = findViewById(R.id.search)
        beginDateCal.setOnClickListener { showDatePickerDialog(MainActivityViewModel.DateType.BEGIN) }
        endDateCal.setOnClickListener { showDatePickerDialog(MainActivityViewModel.DateType.END) }

        search.setOnClickListener{
            var fT="";
            if(flyType.isChecked){ fT= "arrival?"; }
            else{ fT = "departure?" }

            val beginDateTimesTamp = SimpleDateFormat("dd/MM/yy").parse(beginDateLabel.text.toString()).time.toString().substring(0,10).toString()
            val endDateTimesTamp = SimpleDateFormat("dd/MM/yy").parse(endDateLabel.text.toString()).time.toString().substring(0,10).toString()
            val spinner = findViewById<Spinner>(R.id.airportList);
            val spIndex = spinner.selectedItemPosition
            val airportObj = viewModel.getAirportListLiveData().value!![spIndex]
            val airportIcao = airportObj.icao

            var url="https://opensky-network.org/api/flights/"+fT+"airport="+airportIcao+"&begin="+beginDateTimesTamp+"&end="+endDateTimesTamp;

            if (isOnline(this)){
                val i = Intent(this@MainActivity, FlightListActivity::class.java)
                i.putExtra("url", url  )
                i.putExtra("begin", beginDateTimesTamp  )
                i.putExtra("end", endDateTimesTamp  )
                startActivity(i)
            }else{
                Toast.makeText(this, "Veuillez vous connecter à internet.", Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.getAirportNamesListLiveData().observe(this) {
            val adapter = ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, it )
            val airportSpinner = findViewById<Spinner>(R.id.airportList)
            airportSpinner.adapter = adapter
        }

        viewModel.getBeginDateLiveData().observe(this) {
            beginDateLabel.text = Utils.dateToString(it.time)
        }

        viewModel.getEndDateLiveData().observe(this) {
            endDateLabel.text = Utils.dateToString(it.time)
        }
    }

    // open date picker dialog
    private fun showDatePickerDialog(dateType: MainActivityViewModel.DateType) {
        // Date Select Listener.
        val dateSetListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.updateCalendarLiveData(dateType, calendar)
            }

        val currentCalendar = if (dateType == MainActivityViewModel.DateType.BEGIN) viewModel.getBeginDateLiveData().value else viewModel.getEndDateLiveData().value

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            currentCalendar!!.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH),
            currentCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    //check connexion
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


}