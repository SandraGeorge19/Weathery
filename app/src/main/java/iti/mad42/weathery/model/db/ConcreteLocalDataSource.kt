package iti.mad42.weathery.model.db

import android.content.Context
import androidx.lifecycle.LiveData
import io.reactivex.Single
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.WeatherPojo

class ConcreteLocalDataSource(context: Context) : LocalDataSourceInterface {
    private val weatherDAO : WeatherResponseDAO?
    private val favWeatherDAO : FavoriteWeatherDAO?
    private val alarmDAO : AlarmDAO?

    override val getWeatherPojo: LiveData<WeatherPojo>


    override val getAllFavoriteWeathers: LiveData<List<FavoriteWeather>>

    override val allAlarmsLiveList: LiveData<List<AlarmPojo>>?
    override val allAlarmsList: Single<List<AlarmPojo>>?


    init {
        val db : AppDataBase = AppDataBase.getInstance(context)
        weatherDAO =  db.weatherResponseDAO()
        favWeatherDAO = db.favoriteWeatherDAO()
        alarmDAO = db.alarmDAO()
        getWeatherPojo = weatherDAO.getCurrentWeather
        getAllFavoriteWeathers = favWeatherDAO.getAllFavoriteWeather
        allAlarmsList = alarmDAO.allAlarmsList
        allAlarmsLiveList = alarmDAO.allAlarmsLiveList
    }

    //weather response methods
    override fun insertCurrentWeather(weatherPojo: WeatherPojo) {
        weatherDAO?.insertCurrentWeather(weatherPojo)
    }

    //fav weather methods
    override fun addWeatherToFav(favoriteWeather: FavoriteWeather) {
        favWeatherDAO?.addWeatherToFav(favoriteWeather)
    }

    override fun deleteWeatherFromFav(favoriteWeather: FavoriteWeather) {
        favWeatherDAO?.deleteWeatherFromFav(favoriteWeather)
    }


    //alarm weather methods

    override fun getSpecificAlarm(id: Int) : Single<AlarmPojo>?{
        return alarmDAO?.getSpecificAlarm(id)
    }

    override fun insertAlarm(alarmPojo: AlarmPojo) {
        alarmDAO?.insertAlarm(alarmPojo)
    }

    override fun deleteAlarm(alarmPojo: AlarmPojo) {
        alarmDAO?.deleteAlarm(alarmPojo)
    }
}