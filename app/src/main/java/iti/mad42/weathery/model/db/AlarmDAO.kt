package iti.mad42.weathery.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import iti.mad42.weathery.model.pojo.AlarmPojo

@Dao
interface AlarmDAO {

    @get:Query("SELECT * FROM alarm")
    val allAlarmsList : LiveData<List<AlarmPojo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarmPojo: AlarmPojo)

    @Delete
    fun deleteAlarm(alarmPojo: AlarmPojo)
}