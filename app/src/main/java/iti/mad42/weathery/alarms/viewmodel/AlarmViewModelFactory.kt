package iti.mad42.weathery.alarms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import iti.mad42.weathery.model.pojo.RepositoryInterface
import java.lang.IllegalArgumentException

class AlarmViewModelFactory(
    private val _repository: RepositoryInterface
) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlarmViewModel::class.java)){
            AlarmViewModel(_repository) as T
        }else{
            throw IllegalArgumentException("There is No View Model Class Found")
        }
    }
}