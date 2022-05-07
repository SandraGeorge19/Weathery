package iti.mad42.weathery.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import iti.mad42.weathery.model.pojo.FavoriteWeather

@Dao
interface FavoriteWeatherDAO {
    @get:Query("SELECT * FROM favoriteWeather")
    val getAllFavoriteWeather : LiveData<List<FavoriteWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeatherToFav(favoriteWeather: FavoriteWeather)

    @Delete
    fun deleteWeatherFromFav(favoriteWeather: FavoriteWeather)
}