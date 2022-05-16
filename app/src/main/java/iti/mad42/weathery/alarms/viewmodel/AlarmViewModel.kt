package iti.mad42.weathery.alarms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.Single
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(
    var repository: RepositoryInterface
):ViewModel() {
    fun getAllAlarms() : LiveData<List<AlarmPojo>>?{
        return repository.allAlarmsLiveList
    }

    fun addAlarm(alarmPojo: AlarmPojo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlarm(alarmPojo)
        }
    }

    fun deleteAlarm(alarmPojo: AlarmPojo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarmPojo)
        }
    }
}