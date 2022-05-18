package iti.mad42.weathery.model.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.Utility
import iti.mad42.weathery.model.pojo.WeatherPojo

class RemoteDataSource private constructor() : RemoteSourceInterface{
    companion object{
        private var instance : RemoteDataSource? = null
        fun getInstance(): RemoteDataSource{
            return instance?: RemoteDataSource()
        }
    }

    override suspend fun getCurrentTempData(context: Context): WeatherPojo {
        lateinit var weatherPojo: WeatherPojo
        var sharedPreferences : SharedPreferences = context.getSharedPreferences(Utility.latLongSharedPrefKey, AppCompatActivity.MODE_PRIVATE)
        var languageShared : SharedPreferences = context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
        var unitsShared : SharedPreferences = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)
        val retrofitService = RetrofitHelper.getInstance().create(RetrofitInterface::class.java)
        var s = unitsShared.getString("Temp","")!!
        Log.e("sandra", "temp value in shared: $s", )

        val response = retrofitService.getCurrentTempData(
            sharedPreferences.getFloat("GPSLat", 0.0F).toDouble(), sharedPreferences.getFloat("GPSLong", 0.0F).toDouble(), "4a059725f93489b95183bbcb8c6829b9", unitsShared.getString(Utility.TEMP_KEY,"metric").toString(), languageShared.getString(Utility.Language_Key, "en")!!
        )
        Log.e("sandra", "temp value in shared lat: ${sharedPreferences.getFloat("GPSLat", 0.0F).toDouble()}")
        Log.e("sandra", "temp value in shared long: ${sharedPreferences.getFloat("GPSLong", 0.0F).toDouble()}", )
        if (response.isSuccessful == true){
             weatherPojo = response.body()!!
            Log.i("sandra", "getCurrentTempData: ${weatherPojo.current}")
        }
        return weatherPojo
    }

    override suspend fun getFavTempData(favPojo: FavoriteWeather, context: Context): WeatherPojo {
        lateinit var favWeatherDetails : WeatherPojo
        var languageShared : SharedPreferences = context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
        var unitsShared : SharedPreferences = context.getSharedPreferences("Units", AppCompatActivity.MODE_PRIVATE)

        val retrofitService = RetrofitHelper.getInstance().create(RetrofitInterface::class.java)
        val response = retrofitService.getFavTempData(favPojo.lat, favPojo.lon, "4a059725f93489b95183bbcb8c6829b9" , unitsShared.getString(Utility.TEMP_KEY,"metric").toString(), languageShared.getString(Utility.Language_Key, "en")!!)
        if(response.isSuccessful == true){
            favWeatherDetails = response.body()!!
        }
        return favWeatherDetails
    }

}