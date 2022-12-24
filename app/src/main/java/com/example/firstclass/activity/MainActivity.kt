package com.example.firstclass.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.firstclass.R
import com.example.firstclass.utils.*
import com.example.firstclass.viewmodel.MainActivityViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var beginDateLabel: TextView
    private lateinit var endDateLabel: TextView
    private lateinit var beginDateCal: ImageView
    private lateinit var endDateCal: ImageView
    private lateinit var flyType : Switch
    private lateinit var search : Button
    private lateinit var btnDatePicker : FloatingActionButton
    private lateinit var showDatetv: TextView
    var firstDate:Long = 0
    var secondDate:Long=0
    var firstDateall:Long=0
    var secondDateall:Long=0


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //beginDateLabel = findViewById<TextView>(R.id.beginDate)
        //endDateLabel = findViewById<TextView>(R.id.endDate)
        //beginDateCal = findViewById(R.id.beginDateCalendar);
        //endDateCal = findViewById(R.id.endDateCalendar);
        flyType = findViewById(R.id.switchBtn)
        search = findViewById(R.id.search)
        btnDatePicker=findViewById(R.id.floatingActionButton)
        showDatetv=findViewById(R.id.selectedDate)

        //beginDateCal.setOnClickListener { showDatePickerDialog(MainActivityViewModel.DateType.BEGIN) }
        //endDateCal.setOnClickListener { showDatePickerDialog(MainActivityViewModel.DateType.END) }


        btnDatePicker.setOnClickListener {


            val datePicker = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setCalendarConstraints(oneMonthBeforeTodayConstraints()!!.build())

                .setTitleText("Select dates")
                .build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {

                var first=it.first.toString()

                first = first.substring(0, Math.min(first.length, 10))
                var second=it.second.toString()
                second = second.substring(0, Math.min(second.length, 10))

                firstDate=first.toLong()
                secondDate=second.toLong()
                firstDateall=it.first
                secondDateall=it.second

                //Toast.makeText(this, "${datePicker.headerText} is selected", Toast.LENGTH_SHORT).show()
                showDatetv.text= datePicker.headerText

            }

            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {

                //Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_SHORT).show()
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
                Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_SHORT).show()
            }
        }





        search.setOnClickListener{

            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val currentDateAndTime = sdf.format(Date())


            /// Checking if dates are selected ///
            if (firstDate==0L && secondDate==0L){
                showDatetv.text= "Date range must be selected"
                showDatetv.setTextColor(Color.parseColor("#FF0000"))
            }else if(currentDateAndTime == convertLongToTime(secondDateall)){

                showDatetv.text= "Today's date can't be selected"
                showDatetv.setTextColor(Color.parseColor("#FF0000"))
            }
            else if(getDatedifference(firstDateall,secondDateall)>7L){
                showDatetv.text= "Range must be <= 7"
                showDatetv.setTextColor(Color.parseColor("#FF0000"))
            }
            else{
                var fT="";
                if(flyType.isChecked){ fT= "arrival?"; }
                else{ fT = "departure?" }

                //val beginDateTimesTamp = SimpleDateFormat("dd/MM/yy").parse(beginDateLabel.text.toString()).time.toString().substring(0,10).toString()
                //val endDateTimesTamp = SimpleDateFormat("dd/MM/yy").parse(endDateLabel.text.toString()).time.toString().substring(0,10).toString()
                val spinner = findViewById<Spinner>(R.id.airportList);
                val spIndex = spinner.selectedItemPosition
                val airportObj = viewModel.getAirportListLiveData().value!![spIndex]
                val airportIcao = airportObj.icao

                var url="https://opensky-network.org/api/flights/"+fT+"airport="+airportIcao+"&begin="+firstDate+"&end="+secondDate;

                if (isOnline(this)){


                    val i = Intent(this@MainActivity, FlightListActivity::class.java)
                    i.putExtra("url", url.toString()  )
                    i.putExtra("begin", firstDate.toString()  )
                    i.putExtra("end", secondDate.toString()  )
                    startActivity(i)

                }else{
                    Toast.makeText(this, "Veuillez vous connecter à internet.", Toast.LENGTH_SHORT).show()
                }
            }


        }

        viewModel.getAirportNamesListLiveData().observe(this) {

            val adapter = ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, it )
            val airportSpinner = findViewById<Spinner>(R.id.airportList)
            airportSpinner.adapter = adapter

        }

        /*

        viewModel.getBeginDateLiveData().observe(this) {

            beginDateLabel.text = Utils.dateToString(it.time)

        }

        viewModel.getEndDateLiveData().observe(this) {

            endDateLabel.text = Utils.dateToString(it.time)

        }
        */
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(
            "dd-MM-yyyy")
        return format.format(date)
    }
    //Checking if the range date is <=7
    private fun getDatedifference(fd:Long,sd:Long):Long{

        val CurrentDate = convertLongToTime(fd)
        val FinalDate = convertLongToTime(sd)
        val date1: Date
        val date2: Date
        val dates = SimpleDateFormat("dd-MM-yyyy")
        date1 = dates.parse(CurrentDate)
        date2 = dates.parse(FinalDate)
        val difference = Math.abs(date1.time - date2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        //val dayDifference = java.lang.Long.toString(differenceDates)
        return differenceDates

    }


    /// To get only 30 days from today on the calendar ///
    private fun oneMonthBeforeTodayConstraints(): CalendarConstraints.Builder? {
        val constraintsBuilderRange = CalendarConstraints.Builder()
        val maxDate = Calendar.getInstance()
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.DAY_OF_MONTH, -30) // subtracting 30 days
        constraintsBuilderRange.setStart(minDate.timeInMillis)
        constraintsBuilderRange.setEnd(maxDate.timeInMillis)
        constraintsBuilderRange.setValidator(
            RangeValidator(
                minDate.timeInMillis,
                maxDate.timeInMillis
            )
        )
        return constraintsBuilderRange
    }


    /*
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
    */

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


/// To get only 30 days from today on the calendar ///

internal class RangeValidator : DateValidator {
    var minDate: Long = 0
    var maxDate: Long = 0

    constructor(minDate: Long, maxDate: Long) {
        this.minDate = minDate
        this.maxDate = maxDate
    }

    constructor(parcel: Parcel?) {}

    override fun isValid(date: Long): Boolean {
        return !(minDate > date || maxDate < date)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RangeValidator?> =
            object : Parcelable.Creator<RangeValidator?> {
                override fun createFromParcel(parcel: Parcel): RangeValidator? {
                    return RangeValidator(parcel)
                }

                override fun newArray(size: Int): Array<RangeValidator?> {
                    return arrayOfNulls(size)
                }
            }
    }
}