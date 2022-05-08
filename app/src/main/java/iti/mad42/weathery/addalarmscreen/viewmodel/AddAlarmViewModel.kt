package iti.mad42.weathery.addalarmscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAlarmViewModel(
    var repository: RepositoryInterface
): ViewModel() {

    fun addAlarm(alarmPojo: AlarmPojo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlarm(alarmPojo)
        }
    }
}