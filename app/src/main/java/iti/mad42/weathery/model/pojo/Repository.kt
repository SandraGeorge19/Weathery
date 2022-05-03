package iti.mad42.weathery.model.pojo

import android.content.Context
import android.util.Log
import iti.mad42.weathery.model.network.RemoteSourceInterface

class Repository private constructor(
    var remoteSource : RemoteSourceInterface,
    var context : Context?
) : RepositoryInterface{

    companion object{
        private var instance : Repository ?= null
        fun getInstance(remoteSource: RemoteSourceInterface, context: Context?): Repository{
            return instance?: Repository(remoteSource , context)
        }
    }

    override suspend fun getCurrentTempData(): WeatherPojo? {
        Log.i("sandra", "getCurrentTempData: repo : ${remoteSource.getCurrentTempData()?.timezone}")
        return remoteSource.getCurrentTempData()
    }
}