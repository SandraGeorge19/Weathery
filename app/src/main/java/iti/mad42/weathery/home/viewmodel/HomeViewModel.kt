package iti.mad42.weathery.home.viewmodel

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

    init {
        getWeatherForCurrentLocation()
    }

    fun getWeatherForCurrentLocation(){
        viewModelScope.launch(Dispatchers.IO) {
            var weather = repository.getCurrentTempData()
            _mutableWeatherPojo.postValue(weather)
            weather.locationId = "id"
            repository.insertCurrentWeather(weather)
        }
    }

    fun getLocalWeather() : LiveData<WeatherPojo>{
        return repository.getWeatherPojo
    }
}