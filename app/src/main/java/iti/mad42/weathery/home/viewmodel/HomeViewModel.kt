package iti.mad42.weathery.home.viewmodel

import android.util.Log
import androidx.lifecycle.*
import iti.mad42.weathery.model.pojo.RepositoryInterface
import iti.mad42.weathery.model.pojo.WeatherPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {
    private val _mutableWeatherPojo = MutableLiveData<WeatherPojo>()
    var weatherPojo : LiveData<WeatherPojo> = _mutableWeatherPojo

//    init {
//        getWeatherForCurrentLocation()
//    }

    fun getWeatherForCurrentLocation() : LiveData<WeatherPojo>{
        viewModelScope.launch(Dispatchers.IO) {
            var weather = repository.getCurrentTempData()
            _mutableWeatherPojo.postValue(weather)

            weather.locationId = "id"
            Log.e("sandra", "getWeatherForCurrentLocation: lat ${weather.lat}", )
            Log.e("sandra", "getWeatherForCurrentLocation: lon ${weather.lon}", )
            repository.insertCurrentWeather(weather)
        }
        return weatherPojo
    }

    fun getLocalWeather() : LiveData<WeatherPojo>{
        return repository.getWeatherPojo
    }
}