package iti.mad42.weathery.model.network

import iti.mad42.weathery.model.pojo.WeatherPojo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {
    //"onecall?lat=29.9604&lon=31.2885&appid=4a059725f93489b95183bbcb8c6829b9&units=metric&lang=en"
    @GET("onecall")
    suspend fun getCurrentTempData(@Query("lat") latitude : Double, @Query("lon") longitude : Double, @Query("appid") appid : String, @Query("units") units : String, @Query("lang") lang : String): Response<WeatherPojo>

    @GET("onecall")
    suspend fun getFavTempData(@Query("lat") latitude : Double, @Query("lon") longitude : Double, @Query("appid") appid : String, @Query("units") units : String, @Query("lang") lang : String): Response<WeatherPojo>

}