package iti.mad42.weathery.model.pojo

import androidx.lifecycle.LiveData
import iti.mad42.weathery.model.network.RemoteSourceInterface

interface RepositoryInterface {
    // Retrofit Functions
    suspend fun getCurrentTempData() : WeatherPojo

    //LocalDataBase Functions
    //weatherPojo fun
    val getWeatherPojo : LiveData<WeatherPojo>
    fun insertCurrentWeather(weatherPojo: WeatherPojo)

    //weatherFavorite fun
    val getAllFavoriteWeathers : LiveData<List<FavoriteWeather>>
    fun addWeatherToFav(favoriteWeather: FavoriteWeather)
    fun deleteWeatherFromFav(favoriteWeather: FavoriteWeather)

    //alarm fun
    val allAlarmsList : LiveData<List<AlarmPojo>>
    fun insertAlarm(alarmPojo: AlarmPojo)
    fun deleteAlarm(alarmPojo: AlarmPojo)

}