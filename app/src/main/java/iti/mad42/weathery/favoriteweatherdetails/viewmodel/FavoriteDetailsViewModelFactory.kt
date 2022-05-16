package iti.mad42.weathery.favoriteweatherdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import iti.mad42.weathery.model.pojo.RepositoryInterface
import java.lang.IllegalArgumentException

class FavoriteDetailsViewModelFactory (private val _repository : RepositoryInterface)
    : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavoriteDetailsViewModel::class.java)){
            FavoriteDetailsViewModel(_repository) as T
        }else{
            throw IllegalArgumentException("There is No View Model Class Found")
        }
    }
}