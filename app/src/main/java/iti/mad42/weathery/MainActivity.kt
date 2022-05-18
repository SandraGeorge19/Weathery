package iti.mad42.weathery

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.gms.location.*
import iti.mad42.weathery.alarms.view.AlarmFragment
import iti.mad42.weathery.favourites.view.FavoritesFragment
import iti.mad42.weathery.home.view.HomeFragment
import iti.mad42.weathery.model.pojo.Utility
import iti.mad42.weathery.settings.view.SettingFragment

class MainActivity : AppCompatActivity() {
    lateinit var bubbleNavigationLinearView: BubbleNavigationLinearView
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var PERMISSION_ID = 100
    var latitude : Double? = null
    var longitude : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //for location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getFreshLocation()

        //comment
        bubbleNavigationLinearView = findViewById(R.id.bubbleNavigationBar)

        setFragment(HomeFragment())
        bubbleNavigationLinearView.setNavigationChangeListener{ view, position ->
            when(position){
                0 -> setFragment(HomeFragment())
                1 -> setFragment(FavoritesFragment())
                2 -> setFragment(AlarmFragment())
                3 -> setFragment(SettingFragment())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getFreshLocation()
    }

    fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID){
            Log.i("sandra", "onRequestPermissionsResult: $requestCode")
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getFreshLocation()
            }
        }
    }

    fun getFreshLocation(){
        if(checkLocationPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    var location : Location = it.result
                    if(location == null){
                        requestNewLocation()
                    }else{
                        latitude = location.latitude
                        longitude = location.longitude
                        Utility.saveToSharedPref(applicationContext,"GPSLat", latitude!!)
                        Utility.saveToSharedPref(applicationContext,"GPSLong", longitude!!)
                        Utility.saveOverlayPermission(applicationContext, "overlay", false)
//                        Utility.saveLanguageToSharedPref(applicationContext, "Lang", "en")
//                        Utility.saveTempToSharedPref(applicationContext, "Temp", "metric")
                        Log.i("Sandra", "getFreshLocation: $latitude and long : $longitude")

                        //add them to shared prefs
                    }
                }
            }
        }else{
            requestPermissionFromUser()
        }
    }

    private fun checkLocationPermission() : Boolean{
        return (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION).equals(
            PackageManager.PERMISSION_GRANTED)
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION).equals(
            PackageManager.PERMISSION_GRANTED)
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET).equals(
            PackageManager.PERMISSION_GRANTED)
                )
    }

    private fun isLocationEnabled() : Boolean{
        var locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocation(){
        var locationRequest : LocationRequest = LocationRequest.create()
        locationRequest.interval = 10
        locationRequest.fastestInterval = 5
        locationRequest.numUpdates = 1

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper()!!)
    }

    private var locationCallBack : LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            var location : Location = p0.lastLocation
            latitude = location.latitude
            longitude = location.longitude
            Utility.saveToSharedPref(applicationContext,"GPSLat", latitude!!)
            Utility.saveToSharedPref(applicationContext,"GPSLong", longitude!!)
            Utility.saveOverlayPermission(applicationContext, "overlay", false)
//            Utility.saveLanguageToSharedPref(applicationContext, "Lang", "en")
//            Utility.saveTempToSharedPref(applicationContext, "Temp", "metric")
            Log.i("Sandra", "call back: $latitude and long : $longitude")

        }
    }
    private fun requestPermissionFromUser(){
        ActivityCompat.requestPermissions(this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.INTERNET
            ),
            PERMISSION_ID)
    }
}