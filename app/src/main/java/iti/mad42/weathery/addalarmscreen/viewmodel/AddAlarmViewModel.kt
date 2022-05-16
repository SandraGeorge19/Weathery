package iti.mad42.weathery.addalarmscreen.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddAlarmViewModel(
    var repository: RepositoryInterface
): ViewModel() {

    fun addAlarm(alarmPojo: AlarmPojo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlarm(alarmPojo)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDays(startDate : String, endDate : String, time : Int) : List<String>{
        val dtf = DateTimeFormat.forPattern("dd-MM-yyyy")
        val start = dtf.parseLocalDate(startDate)
        val end = dtf.parseLocalDate(endDate).plusDays(1)


        var alarmDays : ArrayList<String> = ArrayList()

        val days = Days.daysBetween(
            org.joda.time.LocalDate(start),
            org.joda.time.LocalDate(end)
        ).days.toLong()

        var i = 0
        while (i < days) {
            val current: org.joda.time.LocalDate = start.plusDays(i)
            val date = current.toDateTimeAtStartOfDay().toString("dd-MM-yyyy")
            alarmDays.add(date)
            i += time
        }
        return alarmDays
    }

}