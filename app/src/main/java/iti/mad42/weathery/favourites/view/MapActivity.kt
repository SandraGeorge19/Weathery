package iti.mad42.weathery.favourites.view

import android.content.Context
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFavFactoryAndViewModel()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        onClickAddToFavBtn(favPlace)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        favMap = googleMap
        favMap.setOnMapClickListener {
            var markerOptions : MarkerOptions = MarkerOptions().position(it).title(getAddressAndDateForLocation(it.latitude, it.longitude))
            favMap.clear()
            favMap.addMarker(markerOptions)
            favMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            Log.e("sandra", "onMapReady: lat is ${it.latitude} and long is ${it.longitude}", )
            favPlace = FavoriteWeather(getAddressAndDateForLocation(it.latitude,it.longitude), it.latitude, it.longitude)
            onClickConfirmAddToFavBtn(favPlace)
        }

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

    private fun initFavFactoryAndViewModel(){
        favPlaceFactory = FavoritesViewModelFactory(Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(this), this))
        favPlaceVM = ViewModelProvider(this,favPlaceFactory).get(FavoritesViewModel::class.java)
    }


    override fun onClickConfirmAddToFavBtn(favPlace: FavoriteWeather) {
        binding.saveFavBtn.setOnClickListener {
            favPlaceVM.addPlaceToFav(favPlace)
            finish()
        }
    }
}