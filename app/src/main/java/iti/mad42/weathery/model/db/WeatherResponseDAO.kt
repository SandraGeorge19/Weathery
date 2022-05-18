package iti.mad42.weathery.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.WeatherPojo

@Dao
interface WeatherResponseDAO {
    @get:Query("SELECT * FROM weather WHERE locationId = 'id'")
    val getCurrentWeather : LiveData<WeatherPojo>

    @Query("SELECT * FROM weather WHERE locationId = 'id'")
    suspend fun getStoredWeather() : WeatherPojo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(weatherPojo: WeatherPojo)

}