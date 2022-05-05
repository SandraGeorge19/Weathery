package iti.mad42.weathery.model.network

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        val retrofitService = RetrofitHelper.getInstance().create(RetrofitInterface::class.java)
        val response = retrofitService.getCurrentTempData(context.getSharedPreferences("LatLong", AppCompatActivity.MODE_PRIVATE).getFloat("GPSLat", 0.0F).toDouble(), context.getSharedPreferences("LatLong", AppCompatActivity.MODE_PRIVATE).getFloat("GPSLong", 0.0F).toDouble(), "4a059725f93489b95183bbcb8c6829b9", "metric", "en")
        if (response.isSuccessful == true){
             weatherPojo = response.body()!!
            Log.i("sandra", "getCurrentTempData: ${weatherPojo.current}")
        }
        return weatherPojo
    }

}