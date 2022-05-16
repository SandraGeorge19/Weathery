package iti.mad42.weathery.favoriteweatherdetails.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.R
import iti.mad42.weathery.broadcast.NetworkChangeReceiver
import iti.mad42.weathery.databinding.ActivityFavoriteWeatherDetailsBinding
import iti.mad42.weathery.favoriteweatherdetails.viewmodel.FavoriteDetailsViewModel
import iti.mad42.weathery.favoriteweatherdetails.viewmodel.FavoriteDetailsViewModelFactory
import iti.mad42.weathery.home.view.TodayTempHoursAdapter
import iti.mad42.weathery.home.view.WeekTempAdapter
import iti.mad42.weathery.home.viewmodel.HomeViewModel
import iti.mad42.weathery.home.viewmodel.HomeViewModelFactory
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.pojo.*
import java.io.IOException
import java.util.*

class FavoriteWeatherDetailsActivity : AppCompatActivity() {

    lateinit var binding : ActivityFavoriteWeatherDetailsBinding
    private lateinit var todayHoursAdapter : TodayTempHoursAdapter
    private lateinit var weekTempAdapter: WeekTempAdapter
    lateinit var detailsVM : FavoriteDetailsViewModel
    lateinit var detailsVMFactory : FavoriteDetailsViewModelFactory
    lateinit var weatherPojo : WeatherPojo
    lateinit var favPojo : FavoriteWeather

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteWeatherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickBack()
        initFactoryAndViewModel()
        favPojo = intent.getSerializableExtra("fromFavToDetails") as FavoriteWeather
        binding.favDetailsTitle.text = favPojo.favLocationName
        initHoursRecycler()
        initWeekRecycler()
        getCurrentWeatherForFav()
    }

    fun onClickBack(){
        binding.backBtn.setOnClickListener{
            finish()
        }
    }

    fun initFactoryAndViewModel(){
        detailsVMFactory = FavoriteDetailsViewModelFactory(Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(this) ,this))
        detailsVM = ViewModelProvider(this, detailsVMFactory).get(FavoriteDetailsViewModel::class.java)
    }

    fun initHoursRecycler(){
        binding.todayTempRecycler
        todayHoursAdapter = TodayTempHoursAdapter(listOf<CurrentWeather>(), this)
        binding.todayTempRecycler.setHasFixedSize(true)
        binding.todayTempRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = todayHoursAdapter
        }
    }

    fun initWeekRecycler(){
        binding.allWeekTempRecycler
        weekTempAdapter = WeekTempAdapter(listOf<DailyWeather>(), this)
        binding.allWeekTempRecycler.setHasFixedSize(true)
        binding.allWeekTempRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = weekTempAdapter
        }
    }

    fun getCurrentWeatherForFav(){
        if(NetworkChangeReceiver.isOnline){
            detailsVM.getWeatherForFav(favPojo).observe(this){
                if(it != null){
                    updateUIWithWeatherData(it)
                }
            }
        }
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

        binding.locationName.isVisible = false
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
}