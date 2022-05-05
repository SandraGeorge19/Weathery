package iti.mad42.weathery.model.pojo

import iti.mad42.weathery.model.network.RemoteSourceInterface

interface RepositoryInterface {
    // Retrofit Functions
    suspend fun getCurrentTempData() : WeatherPojo
}