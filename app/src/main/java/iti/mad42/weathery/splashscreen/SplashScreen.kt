package iti.mad42.weathery.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView
import com.google.android.gms.location.*
import iti.mad42.weathery.MainActivity
import iti.mad42.weathery.R
import iti.mad42.weathery.broadcast.NetworkChangeReceiver
import iti.mad42.weathery.databinding.ActivitySplashScreenBinding
import java.util.jar.Manifest

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    lateinit var weather : PrecipType



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for internet connection
        initConnectionListener()

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setSnowAnimation(binding)
        setLogoAnimated(binding)
    }

    //setting broadcast receiver for connection listener
    private fun initConnectionListener(){
        var intentFilter : IntentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(NetworkChangeReceiver(), intentFilter)
    }

    fun setSnowAnimation(binding: ActivitySplashScreenBinding){
        weather = PrecipType.SNOW
        binding.wvWeather.apply {
            setWeatherData(weather)
            speed = 300
            emissionRate = 20f
            angle = 45
            fadeOutPercent = .85f
        }
    }
    fun setLogoAnimated(binding: ActivitySplashScreenBinding){
        binding.logoName.animate().translationX(2000F).setDuration(1000).setStartDelay(4000)
        binding.logo.animate().translationX(1500F).setDuration(1000).setStartDelay(4000)
        Handler().postDelayed(Runnable {
            startActivity(Intent( this, MainActivity::class.java))
            finish() }, 5000)
    }

}