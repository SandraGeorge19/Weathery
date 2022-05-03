package iti.mad42.weathery.model.network

import android.util.Log
import iti.mad42.weathery.model.pojo.WeatherPojo

class RemoteDataSource private constructor() : RemoteSourceInterface{
    companion object{
        private var instance : RemoteDataSource? = null
        fun getInstance(): RemoteDataSource{
            return instance?: RemoteDataSource()
        }
    }

    override suspend fun getCurrentTempData(): WeatherPojo? {
        var weatherPojo : WeatherPojo? = null
        val retrofitService = RetrofitHelper.getInstance().create(RetrofitInterface::class.java)
        val response = retrofitService.getCurrentTempData()
        if (response?.isSuccessful == true){
            weatherPojo = response?.body()
            Log.i("sandra", "getCurrentTempData: ${weatherPojo?.current}")
        }
        return weatherPojo
    }

}