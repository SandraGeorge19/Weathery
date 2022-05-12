package iti.mad42.weathery.model.pojo

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Utility {
    //api key map
    //AIzaSyBDIon-VAldKAVRgh_V5MGSBgi1_NYNO9E
    companion object{
        val latLongSharedPrefKey : String = "LatLong"
        val GPSLatKey : String = "GPSLat"
        val GPSLongKey : String = "GPSLong"
        val Language_EN_Value : String = "en"
        val Language_AR_Value : String = "ar"
        val Language_Key : String = "Lang"
        val ERROR_DIALOG_REQUEST = 5555

        val ALERT_TYPE_ARRAY = arrayOf("Rain", "Snow", "Cloud", "Wind", "Thunder Storm","Mist / Fog","Fire warning")

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

        fun saveToSharedPref(context: Context ,key : String, value : Double){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("LatLong",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putFloat(key, value.toFloat())
            editor.apply()
        }

        fun saveLanguageToSharedPref(context: Context, key : String, value : String){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("Language",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun saveTempToSharedPref(context: Context, key : String, value : String){
            var editor : SharedPreferences.Editor = context.getSharedPreferences("Units",
                AppCompatActivity.MODE_PRIVATE
            ).edit()
            editor.putString(key, value)
            editor.apply()
        }


    }
}