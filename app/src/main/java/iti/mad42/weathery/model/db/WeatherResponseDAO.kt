package iti.mad42.weathery.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.WeatherPojo

@Dao
interface WeatherResponseDAO {
    @get:Query("SELECT * FROM weather")
    val getCurrentWeather : LiveData<WeatherPojo>
}