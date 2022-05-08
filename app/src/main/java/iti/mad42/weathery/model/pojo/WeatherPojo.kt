package iti.mad42.weathery.model.pojo

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "weather")
data class WeatherPojo(
    @PrimaryKey
    @NotNull
    var locationId : String,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: CurrentWeather,
    val hourly: List<CurrentWeather>,
    val daily: List<DailyWeather>,
    val alerts : List<Alert>?
) : Serializable
