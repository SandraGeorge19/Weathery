package iti.mad42.weathery.favoriteweatherdetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.ActivityFavoriteWeatherDetailsBinding

class FavoriteWeatherDetailsActivity : AppCompatActivity() {

    lateinit var binding : ActivityFavoriteWeatherDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteWeatherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}