package iti.mad42.weathery.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "alarm")
data class AlarmPojo(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id : Int = 0,
    var alarmTitle : String,
    var alarmStartDate : Long,
    var alarmEndDate : Long,
    var alarmType : String,
    var isNotification : Boolean
) : Serializable
