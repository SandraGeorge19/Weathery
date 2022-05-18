package iti.mad42.weathery.splashscreen

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView
import com.google.android.gms.location.*
import iti.mad42.weathery.MainActivity
import iti.mad42.weathery.R
import iti.mad42.weathery.broadcast.NetworkChangeReceiver
import iti.mad42.weathery.databinding.ActivitySplashScreenBinding
import iti.mad42.weathery.favourites.view.MapActivity
import iti.mad42.weathery.model.pojo.Utility
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.jar.Manifest

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    lateinit var weather : PrecipType
    lateinit var initialSettingDialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for internet connection
        initConnectionListener()
        initialSettingDialog = Dialog(this)
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
            if (!isFirstTime()){
                Log.e("sandra", "onCreate: not first", )
                goToMainScreen()
            }
            else{
                Log.e("sandra", "onCreate: first", )
                openInitialSettingDialog()
            } }, 5000)
    }

    fun isFirstTime() : Boolean{
        var firstTime : SharedPreferences = getSharedPreferences("first", MODE_PRIVATE)
        Log.e("sandra", "isFirstTime: isFirstTime ${firstTime.getBoolean("first", true)}", )
        return firstTime.getBoolean("first", true)
    }

    fun goToMainScreen(){
        startActivity(Intent( this, MainActivity::class.java))
        finish()
    }

    fun openInitialSettingDialog(){
        initialSettingDialog.setContentView(R.layout.initial_setting_dialog)
        initialSettingDialog.window?.setBackgroundDrawable((ColorDrawable(Color.TRANSPARENT)))
        var gpsRB : RadioButton = initialSettingDialog.findViewById(R.id.gpsRB)
        var mapRB : RadioButton = initialSettingDialog.findViewById(R.id.mapRB)
        var locationGroup : RadioGroup = initialSettingDialog.findViewById(R.id.locationGroup)
        var setBtn : Button = initialSettingDialog.findViewById(R.id.setInitBtn)

        setBtn.setOnClickListener {
            if(gpsRB.isChecked or mapRB.isChecked){
                when(locationGroup.checkedRadioButtonId){
                    R.id.gpsRB -> {
                        goToMainScreen()
                        Utility.saveFirstTimeEnterAppSharedPref(applicationContext, "first", false)
                    }
                    R.id.mapRB -> {
                        goToMapActivity()
                        Utility.saveFirstTimeEnterAppSharedPref(applicationContext, "first", false)
                    }
                }
                initialSettingDialog.dismiss()
            }else{
                Toast.makeText(this, getString(R.string.choose_map_gps_msg), Toast.LENGTH_SHORT).show()
            }
        }
        initialSettingDialog.show()
    }

    fun goToMapActivity(){
        Utility.saveIsMapSharedPref(this, "isMap", true)
        var mapIntent : Intent = Intent(this, MapActivity::class.java)
        startActivity(mapIntent)
        finish()
    }

}