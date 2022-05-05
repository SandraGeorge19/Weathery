package iti.mad42.weathery.model.network

import android.content.Context
import iti.mad42.weathery.model.pojo.WeatherPojo

interface RemoteSourceInterface {
    suspend fun getCurrentTempData(context: Context) : WeatherPojo
}