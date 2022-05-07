package iti.mad42.weathery.favourites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {

    fun getFavPlaces() : LiveData<List<FavoriteWeather>>{
        return repository.getAllFavoriteWeathers
    }

    fun addPlaceToFav(favoriteWeather: FavoriteWeather){
        viewModelScope.launch (Dispatchers.IO){
            repository.addWeatherToFav(favoriteWeather)
        }
    }

    fun deletePlaceFromFav(favoriteWeather: FavoriteWeather){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteWeatherFromFav(favoriteWeather)
        }
    }
}