package iti.mad42.weathery.home.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var todayHoursAdapter : TodayTempHoursAdapter
    private lateinit var weekTempAdapter: WeekTempAdapter
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var homeViewModel: HomeViewModel


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
        homeViewModel.weatherPojo.observe(requireActivity()){
            if(it != null){
                updateUIWithWeatherData(it)
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
        //GPSLat GPSLong
        var sharedPreferences : SharedPreferences = requireActivity()!!.getSharedPreferences("LatLong", Context.MODE_PRIVATE)
        var addressGeocoder : Geocoder = Geocoder(context, Locale.getDefault())
        try {
            var myAddress : List<Address> = addressGeocoder.getFromLocation(sharedPreferences.getFloat("GPSLat" , 0.0F).toDouble(), sharedPreferences.getFloat("GPSLong", 0.0F).toDouble(), 2)
            if(myAddress.isNotEmpty()){
                binding.locationName.text = "${myAddress[0].subAdminArea}, ${myAddress[0].adminArea}"
                Log.i("Sandra", "getAddressForLocation: ${myAddress[0].subAdminArea} ${myAddress[0].adminArea}")
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

}