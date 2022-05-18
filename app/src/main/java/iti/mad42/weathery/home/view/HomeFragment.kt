package iti.mad42.weathery.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import iti.mad42.weathery.broadcast.NetworkChangeReceiver
import iti.mad42.weathery.databinding.FragmentHomeBinding
import iti.mad42.weathery.home.viewmodel.HomeViewModel
import iti.mad42.weathery.home.viewmodel.HomeViewModelFactory
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.network.RemoteSourceInterface
import iti.mad42.weathery.model.pojo.*
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*
import android.R





class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var todayHoursAdapter : TodayTempHoursAdapter
    private lateinit var weekTempAdapter: WeekTempAdapter
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    var PERMISSION_ID = 100
    var latitude : Double? = null
    var longitude : Double? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var sharedPreferences : SharedPreferences
    lateinit var addressGeocoder : Geocoder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        var view : View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getFreshLocation()
         sharedPreferences = requireContext().getSharedPreferences("LatLong", Context.MODE_PRIVATE)
        addressGeocoder = Geocoder(requireContext(), Locale.getDefault())
        initFactoryAndViewModel()
        initHoursRecycler()
        initWeekRecycler()
        getCurrentWeather()
    }

    fun initHoursRecycler(){
        binding.todayTempRecycler
        todayHoursAdapter = TodayTempHoursAdapter(listOf<CurrentWeather>(), context)
        binding.todayTempRecycler.setHasFixedSize(true)
        binding.todayTempRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = todayHoursAdapter
        }
    }

    fun initWeekRecycler(){
        binding.allWeekTempRecycler
        weekTempAdapter = WeekTempAdapter(listOf<DailyWeather>(), context)
        binding.allWeekTempRecycler.setHasFixedSize(true)
        binding.allWeekTempRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = weekTempAdapter
        }
    }

    private fun initFactoryAndViewModel(){
        homeViewModelFactory = HomeViewModelFactory(Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(requireContext()) ,requireContext()))
        homeViewModel = ViewModelProvider(requireActivity(), homeViewModelFactory).get(HomeViewModel::class.java)
    }

    fun getCurrentWeather(){
        if(NetworkChangeReceiver.isOnline){
            Log.e("sandra", "online is : ${NetworkChangeReceiver.isOnline}")
            homeViewModel.getWeatherForCurrentLocation().observe(requireActivity()){
                if(it != null){
                    updateUIWithWeatherData(it)
                }
            }
        } else{

            homeViewModel.getLocalWeather().observe(requireActivity()){
                if(it != null){
                    updateUIWithWeatherData(it)
                }
            }
            Snackbar.make(requireView(), getString(iti.mad42.weathery.R.string.no_internet_txt), Snackbar.LENGTH_LONG)
                .setAction("Setting", View.OnClickListener { startActivityForResult( Intent(Settings.ACTION_SETTINGS), 0); }).show()
        }
    }

    override fun onResume() {
        super.onResume()
        getFreshLocation()
        getCurrentWeather()
    }

    fun updateUIWithWeatherData(weatherPojo: WeatherPojo){
        getAddressAndDateForLocation()
        getTodayTemp(weatherPojo)
        todayHoursAdapter.hoursList = weatherPojo.hourly
        todayHoursAdapter.notifyDataSetChanged()
        weekTempAdapter.weekList = weatherPojo.daily
        weekTempAdapter.notifyDataSetChanged()
    }

    fun getAddressAndDateForLocation(){
        //GPSLat GPSLong
//        var sharedPreferences : SharedPreferences = requireContext().getSharedPreferences("LatLong", Context.MODE_PRIVATE)
//        var addressGeocoder : Geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            var myAddress : List<Address> = addressGeocoder.getFromLocation(sharedPreferences.getFloat("GPSLat" , 0.0F).toDouble(), sharedPreferences.getFloat("GPSLong", 0.0F).toDouble(), 2)
            if(myAddress.isNotEmpty()){
                binding.locationName.text = "${myAddress[0].subAdminArea}, ${myAddress[0].adminArea}"
            }
        }catch (e : IOException){
            e.printStackTrace()
        }
    }

    fun getTodayTemp(weatherPojo: WeatherPojo){

        binding.homeDate.text = Utility.timeStampToDate(weatherPojo.current.dt)
        Log.i("Sandra", "getTodayTemp: ${weatherPojo.current.dt}")
        binding.todayTempDegreeTxt.text = "${weatherPojo.current.temp.toInt().toString()} ℃"
        binding.todayTempStatusTxt.text = weatherPojo.current.weather[0].description
//        if(weatherPojo.current.weather[0].description.value != null){
//            binding.todayTempStatusTxt.text = weatherPojo.current.weather[0].description.name
//        }else{
//            binding.todayTempStatusTxt.text = "DDDDD"
//        }
        binding.pressureValueTxt.text = weatherPojo.current.pressure.toString()
        binding.humidityValueTxt.text = "${weatherPojo.current.humidity} %"
        binding.windValueTxt.text = weatherPojo.current.windSpeed.toString()
        binding.cloudValueTxt.text = weatherPojo.current.clouds.toString()
        binding.UVValueTxt.text = weatherPojo.current.uvi.toString()
        binding.visibilityValueTxt.text = weatherPojo.current.visibility.toString()

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

    @SuppressLint("MissingPermission")
    fun getFreshLocation(){
        if(checkLocationPermission()){
            if(isLocationEnabled()){
                requestNewLocation()
            }else{
                enableLocationSetting()
            }
        }else{
            requestPermissionFromUser()
        }
    }

    private fun checkLocationPermission() : Boolean{
        return (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION).equals(
            PackageManager.PERMISSION_GRANTED)
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION).equals(
            PackageManager.PERMISSION_GRANTED)
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.INTERNET).equals(
            PackageManager.PERMISSION_GRANTED)
                )
    }

    private fun isLocationEnabled() : Boolean{
        var locationManager : LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocation(){
        var locationRequest : LocationRequest = LocationRequest.create()
        locationRequest.interval = 10
        locationRequest.fastestInterval = 5
        locationRequest.numUpdates = 1

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper()!!)
    }

    private fun enableLocationSetting() {
        val settingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(settingIntent)
    }

    fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }

    private var locationCallBack : LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            var location : Location = p0.lastLocation
            latitude = location.latitude
            longitude = location.longitude
            checkIfFragmentAttached {
                Utility.saveToSharedPref(requireContext(),"GPSLat", latitude!!)
                Utility.saveToSharedPref(requireContext(),"GPSLong", longitude!!)
                Utility.saveOverlayPermission(requireContext(), "overlay", false)
                Utility.saveFirstTimeEnterAppSharedPref(requireContext(), "first", true)
            }

//            Utility.saveLanguageToSharedPref(applicationContext, "Lang", "en")
//            Utility.saveTempToSharedPref(applicationContext, "Temp", "metric")
            Log.i("Sandra", "call back: $latitude and long : $longitude")

        }
    }
    private fun requestPermissionFromUser(){
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.INTERNET
            ),
            PERMISSION_ID)
    }

}