package iti.mad42.weathery.model.pojo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import iti.mad42.weathery.model.db.LocalDataSourceInterface
import iti.mad42.weathery.model.network.RemoteSourceInterface

class Repository private constructor(
    var remoteSource : RemoteSourceInterface,
    var localDataSource: LocalDataSourceInterface,
    var context : Context
) : RepositoryInterface{

    companion object{
        private var instance : Repository ?= null
        fun getInstance(remoteSource: RemoteSourceInterface, localDataSource: LocalDataSourceInterface, context: Context): Repository{
            return instance?: Repository(remoteSource, localDataSource , context)
        }
    }

    //Network methods
    override suspend fun getCurrentTempData(): WeatherPojo {
        Log.i("sandra", "getCurrentTempData: repo : ${remoteSource.getCurrentTempData(context).timezone}")
        return remoteSource.getCurrentTempData(context)
    }

    //LocalDataSource methods
    //weatherPojo methods

    override val getWeatherPojo: LiveData<WeatherPojo>
        get() = localDataSource.getWeatherPojo

    //favoriteWeather methods
    override val getAllFavoriteWeathers: LiveData<List<FavoriteWeather>>
        get() = localDataSource.getAllFavoriteWeathers

    override fun addWeatherToFav(favoriteWeather: FavoriteWeather) {
        localDataSource.addWeatherToFav(favoriteWeather)
    }

    override fun deleteWeatherFromFav(favoriteWeather: FavoriteWeather) {
        localDataSource.deleteWeatherFromFav(favoriteWeather)
    }

    //alarm methods
    override val allAlarmsList: LiveData<List<AlarmPojo>>
        get() = localDataSource.allAlarmsList

    override fun insertAlarm(alarmPojo: AlarmPojo) {
        localDataSource.insertAlarm(alarmPojo)
    }

    override fun deleteAlarm(alarmPojo: AlarmPojo) {
        localDataSource.deleteAlarm(alarmPojo)
    }
}