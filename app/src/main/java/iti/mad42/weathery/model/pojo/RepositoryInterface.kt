package iti.mad42.weathery.model.pojo

import androidx.lifecycle.LiveData
import io.reactivex.Single
import iti.mad42.weathery.model.network.RemoteSourceInterface

interface RepositoryInterface {
    // Retrofit Functions
    suspend fun getCurrentTempData() : WeatherPojo
    suspend fun getFavWeatherData(favWeather : FavoriteWeather) : WeatherPojo

    //LocalDataBase Functions
    //weatherPojo fun
    val getWeatherPojo : LiveData<WeatherPojo>
    fun insertCurrentWeather(weatherPojo: WeatherPojo)

    //weatherFavorite fun
    val getAllFavoriteWeathers : LiveData<List<FavoriteWeather>>
    fun addWeatherToFav(favoriteWeather: FavoriteWeather)
    fun deleteWeatherFromFav(favoriteWeather: FavoriteWeather)

    //alarm fun
    val allAlarmsLiveList : LiveData<List<AlarmPojo>>?
    val allAlarmsList : Single<List<AlarmPojo>>?
    fun getSpecificAlarm(id : Int) : Single<AlarmPojo>?
    fun insertAlarm(alarmPojo: AlarmPojo)
    fun deleteAlarm(alarmPojo: AlarmPojo)

}