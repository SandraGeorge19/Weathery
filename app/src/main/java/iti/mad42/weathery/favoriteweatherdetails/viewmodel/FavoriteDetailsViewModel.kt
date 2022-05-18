package iti.mad42.weathery.favoriteweatherdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.RepositoryInterface
import iti.mad42.weathery.model.pojo.WeatherPojo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteDetailsViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {
    private val _mutableFavWeather = MutableLiveData<WeatherPojo>()
    var favWeatherPojo : LiveData<WeatherPojo> = _mutableFavWeather

    fun getWeatherForFav(fav : FavoriteWeather) : LiveData<WeatherPojo>{
        viewModelScope.launch (Dispatchers.IO){
            var favWeather = repository.getFavWeatherData(fav)
            _mutableFavWeather.postValue(favWeather)
        }
        return  favWeatherPojo
    }
}