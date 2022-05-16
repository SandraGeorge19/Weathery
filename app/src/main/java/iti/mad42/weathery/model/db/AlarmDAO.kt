package iti.mad42.weathery.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single
import iti.mad42.weathery.model.pojo.AlarmPojo

@Dao
interface AlarmDAO {

    @get:Query("SELECT * FROM alarm")
    val allAlarmsLiveList : LiveData<List<AlarmPojo>>

    @get:Query("SELECT * FROM alarm")
    val allAlarmsList : Single<List<AlarmPojo>>

    @Query("SELECT * FROM alarm WHERE id =:id")
    fun getSpecificAlarm(id : Int) : Single<AlarmPojo>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarmPojo: AlarmPojo)

    @Delete
    fun deleteAlarm(alarmPojo: AlarmPojo)
}