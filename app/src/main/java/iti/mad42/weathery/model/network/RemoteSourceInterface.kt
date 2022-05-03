package iti.mad42.weathery.model.network

import iti.mad42.weathery.model.pojo.WeatherPojo

interface RemoteSourceInterface {
    suspend fun getCurrentTempData() : WeatherPojo?
}