package iti.mad42.weathery.favourites.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import iti.mad42.weathery.MainActivity
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.ActivityMapBinding
import iti.mad42.weathery.favourites.viewmodel.FavoritesViewModel
import iti.mad42.weathery.favourites.viewmodel.FavoritesViewModelFactory
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.network.RemoteSourceInterface
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.Repository
import iti.mad42.weathery.model.pojo.Utility
import java.io.IOException
import java.util.*

class MapActivity : AppCompatActivity() , OnMapReadyCallback, OnClickConfirmAddToFavListener{

    private lateinit var binding: ActivityMapBinding
    private lateinit var favMap : GoogleMap
    lateinit var favPlaceFactory: FavoritesViewModelFactory
    lateinit var favPlaceVM : FavoritesViewModel
    lateinit var favPlace: FavoriteWeather
    var isFav : Boolean =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isFav = intent.getBooleanExtra("isFav", false)
        initFavFactoryAndViewModel()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        onClickAddToFavBtn(favPlace)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        favMap = googleMap
        var markerOptions : MarkerOptions = MarkerOptions().position(LatLng(26.8206,  30.8025)).title(getAddressAndDateForLocation(26.8206,  30.8025))

        favMap.addMarker(markerOptions)
        favMap.setOnMapClickListener {
            var markerOptions : MarkerOptions = MarkerOptions().position(it).title(getAddressAndDateForLocation(it.latitude, it.longitude))
            favMap.clear()
            favMap.addMarker(markerOptions)
            favMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            Log.e("sandra", "onMapReady: lat is ${it.latitude} and long is ${it.longitude}", )
            if(isMapFromSharedPref()){
                Log.e("san", "onMapReady: isMapFromSharedPref ${it.latitude}, ${it.longitude}", )
                Utility.saveToSharedPref(this,"GPSLat", it.latitude)
                Utility.saveToSharedPref(this,"GPSLong", it.longitude)
            }
            favPlace = FavoriteWeather(getAddressAndDateForLocation(it.latitude,it.longitude), it.latitude, it.longitude)
            onClickConfirmAddToFavBtn(favPlace)
        }

    }

    private fun isMapFromSharedPref() : Boolean{
        var isMapShared : SharedPreferences = getSharedPreferences("isMap", MODE_PRIVATE)
        Log.e("sandra", "isMapFromSharedPref: ${isMapShared.getBoolean("isMap", false)}", )
        return isMapShared.getBoolean("isMap", false)
    }

    private fun initFavFactoryAndViewModel(){
        favPlaceFactory = FavoritesViewModelFactory(Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(this), this))
        favPlaceVM = ViewModelProvider(this,favPlaceFactory).get(FavoritesViewModel::class.java)
    }


    fun getAddressAndDateForLocation(lat : Double, lon : Double) : String{
        //GPSLat GPSLong
        var addressGeocoder : Geocoder = Geocoder(this, Locale.getDefault())
        try {
            var myAddress : List<Address> = addressGeocoder.getFromLocation(lat, lon, 2)
            if(myAddress.isNotEmpty()){
                return "${myAddress[0].subAdminArea} ${myAddress[0].adminArea}"
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
        return ""
    }


    override fun onClickConfirmAddToFavBtn(favPlace: FavoriteWeather) {
        binding.saveFavBtn.setOnClickListener {
            var confirmDialog = AlertDialog.Builder(this)
            confirmDialog.setMessage(getString(R.string.add_fav_mgs))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.save_btn), DialogInterface.OnClickListener{
                    dialogInterface, i ->
                        if(isFav){
                            favPlaceVM.addPlaceToFav(favPlace)
                            finish()
                        }else{
                            Utility.saveIsMapSharedPref(this, "isMap", true)
                            saveLatLongInSharedPref(favPlace.lat,favPlace.lon)
                        }
                })
                .setNegativeButton(getString(R.string.cancel_btn), DialogInterface.OnClickListener{
                    dialogInterface, i ->  dialogInterface.cancel()
                })
            val alert = confirmDialog.create()
            alert.setTitle(getString(R.string.confirm_save_txt))
            alert.show()
//            favPlaceVM.addPlaceToFav(favPlace)
//            finish()
        }
    }

    fun saveLatLongInSharedPref(lat : Double, lon : Double){
        Utility.saveToSharedPref(this,"GPSLat", lat)
        Utility.saveToSharedPref(this,"GPSLong", lon)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}