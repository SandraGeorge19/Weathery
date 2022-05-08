package iti.mad42.weathery.addalarmscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import iti.mad42.weathery.model.pojo.RepositoryInterface
import java.lang.IllegalArgumentException

class AddAlarmViewModelFactory(private val _repository : RepositoryInterface) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AddAlarmViewModel::class.java)){
            AddAlarmViewModel(_repository) as T
        }else{
            throw IllegalArgumentException("There is No View Model Class Found")
        }
    }
}