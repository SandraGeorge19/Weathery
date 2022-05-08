package iti.mad42.weathery.model.db

import androidx.lifecycle.LiveData
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.WeatherPojo

interface LocalDataSourceInterface {

    //WeatherResponse methods
    val getWeatherPojo : LiveData<WeatherPojo>
    fun insertCurrentWeather(weatherPojo: WeatherPojo)

    //FavoriteWeather methods
    val getAllFavoriteWeathers : LiveData<List<FavoriteWeather>>
    fun addWeatherToFav(favoriteWeather: FavoriteWeather)
    fun deleteWeatherFromFav(favoriteWeather: FavoriteWeather)

    //Alarm methods
    val allAlarmsList : LiveData<List<AlarmPojo>>
    fun insertAlarm(alarmPojo: AlarmPojo)
    fun deleteAlarm(alarmPojo: AlarmPojo)
}
