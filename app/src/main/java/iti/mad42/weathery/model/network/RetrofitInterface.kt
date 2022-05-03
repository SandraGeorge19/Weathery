package iti.mad42.weathery.model.network

import iti.mad42.weathery.model.pojo.WeatherPojo
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitInterface {
    @GET("onecall?lat=29.9604&lon=31.2885&appid=4a059725f93489b95183bbcb8c6829b9&units=metric&lang=en")
    suspend fun getCurrentTempData(): Response<WeatherPojo>?
}