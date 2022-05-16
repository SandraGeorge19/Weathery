package iti.mad42.weathery.model.pojo

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


//val current: CurrentWeather,
//val hourly: List<CurrentWeather>,
//val daily: List<DailyWeather>
class Converters {
    var gson : Gson = Gson()

    //Convert from / to currentWeather
    @TypeConverter
    fun fromCurrentWeatherToString(currentWeather: CurrentWeather): String{
        return gson.toJson(currentWeather)
    }

    @TypeConverter
    fun fromStringToCurrentWeather(currentWeatherString: String) : CurrentWeather{
        return gson.fromJson(currentWeatherString, CurrentWeather::class.java)
    }

    //convert from / to list of currentWeather
    @TypeConverter
    fun fromCurrentWeatherListToString(currentWeatherList : List<CurrentWeather>) : String{
        return gson.toJson(currentWeatherList)
    }

    @TypeConverter
    fun fromStringToCurrentWeatherList(currentWeatherListString : String) : List<CurrentWeather>{
        if (currentWeatherListString == null){
            return Collections.emptyList()
        }else{
            val list = object : TypeToken<List<CurrentWeather?>?>() {}.type
            return gson.fromJson(currentWeatherListString, list)
        }
    }

    //convert from / to list of dailyWeather

    @TypeConverter
    fun fromDailyWeatherListToString (dailyWeatherList: List<DailyWeather>): String{
        return gson.toJson(dailyWeatherList)
    }

    @TypeConverter
    fun fromStringToDailyWeatherList (dailyWeatherListString: String) : List<DailyWeather>{
        if(dailyWeatherListString == null){
            return Collections.emptyList()
        }else{
            var list = object : TypeToken<List<DailyWeather?>?>(){}.type
            return gson.fromJson(dailyWeatherListString, list)
        }
    }

    //convert from / to weatherDetails
    @TypeConverter
    fun fromWeatherDetailsListToString(weatherDetailsList: List<WeatherDetails>) : String{
        return gson.toJson(weatherDetailsList)
    }

    @TypeConverter
    fun fromStringToWeatherDetailsList(weatherDetailsListString: String) : List<WeatherDetails>{
        if(weatherDetailsListString == null){
            return Collections.emptyList()
        }else{
            var list = object : TypeToken<List<WeatherDetails?>?>(){}.type
            return gson.fromJson(weatherDetailsListString, list)
        }
    }

    //convert from / to favouriteWeather
    @TypeConverter
    fun fromFavoriteWeatherToString(favoriteWeather: FavoriteWeather) : String{
        return gson.toJson(favoriteWeather)
    }

    @TypeConverter
    fun fromStringToFavoriteWeather(favoriteWeatherString: String) : FavoriteWeather{
        return gson.fromJson(favoriteWeatherString, FavoriteWeather::class.java)
    }

    //convert from / to tempAllDay
    @TypeConverter
    fun fromTempAllDayToString(tempAllDay: TempAllDay) : String{
        return gson.toJson(tempAllDay)
    }

    @TypeConverter
    fun fromStringToTempAllDay(tempAllDayString: String) : TempAllDay{
        return gson.fromJson(tempAllDayString, TempAllDay::class.java)
    }

    //convert from / to temp
    @TypeConverter
    fun fromTempToString(temp: Temp) : String{
        return gson.toJson(temp)
    }

    @TypeConverter
    fun fromStringToTemp(tempString: String) : Temp{
        return gson.fromJson(tempString, Temp::class.java)
    }

    //covert from / to alerts
    @TypeConverter
    fun fromAlertToString(alertList: List<Alert>?) : String{
        return gson.toJson(alertList)
    }

    @TypeConverter
    fun fromStringToAlert(alertListString: String) : List<Alert>?{
        if(alertListString == null){
            return Collections.emptyList()
        }else{
            var list = object : TypeToken<List<Alert?>?>(){}.type
            return gson.fromJson(alertListString, list)
        }
    }

    //convert from / to list of alarm days
    @TypeConverter
    open fun fromAlarmDaysToString(alarmDaysList: List<String?>?): String? {
        return gson.toJson(alarmDaysList)
    }

    @TypeConverter
    fun fromStringToAlarmDays(alarmDaysString: String?): List<String?>? {
        return if (alarmDaysString == null) {
            emptyList<String>()
        } else {
            val list = object : TypeToken<List<String?>?>() {}.type
            gson.fromJson(alarmDaysString, list)
        }
    }
}