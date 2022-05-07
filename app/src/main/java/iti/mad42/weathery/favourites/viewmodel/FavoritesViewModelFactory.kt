package iti.mad42.weathery.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import iti.mad42.weathery.home.viewmodel.HomeViewModel
import iti.mad42.weathery.model.pojo.RepositoryInterface
import java.lang.IllegalArgumentException

class FavoritesViewModelFactory(private val _repository : RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavoritesViewModel::class.java)){
            FavoritesViewModel(_repository) as T
        }else{
            throw IllegalArgumentException("There is No View Model Class Found")
        }
    }

}