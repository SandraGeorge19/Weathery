package iti.mad42.weathery.model.pojo

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Utility {
    companion object{
        val sharedPrefKey : String = "LatLong"
        val GPSLatKey : String = "GPSLat"
        val GPSLongKey : String = "GPSLong"

        fun timeStampToDate (dt : Long) : String{
            var date : Date = Date(dt * 1000)
            var dateFormat : DateFormat = SimpleDateFormat("MMM d, yyyy")
            return dateFormat.format(date)
        }
        fun timeStampToDay(dt: Long) : String{
            var date: Date = Date(dt * 1000)
            var dateFormat : DateFormat = SimpleDateFormat("EEEE")
            return dateFormat.format(date)
        }
        fun timeStampToHour(dt : Long) : String{
            var date: Date = Date(dt * 1000)
            var dateFormat : DateFormat = SimpleDateFormat("h:mm a")
            return dateFormat.format(date)
        }
        fun timeStampToHourOneNumber(dt : Long) : String{
            var date: Date = Date(dt * 1000)
            var dateFormat : DateFormat = SimpleDateFormat("h")
            return dateFormat.format(date)
        }
    }
}