package iti.mad42.weathery.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.Converters
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.WeatherPojo

@TypeConverters(Converters::class)
@Database(entities = [WeatherPojo::class, FavoriteWeather::class, AlarmPojo::class] , version = 1)
abstract class AppDataBase : RoomDatabase(){


    abstract fun weatherResponseDAO(): WeatherResponseDAO
    abstract fun favoriteWeatherDAO(): FavoriteWeatherDAO
    abstract fun alarmDAO(): AlarmDAO

    companion object{
        private val DATABASE_NAME = "Weather_App_DB"
        private var instance : AppDataBase? = null
        fun getInstance(context: Context): AppDataBase{
            return instance?: Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}