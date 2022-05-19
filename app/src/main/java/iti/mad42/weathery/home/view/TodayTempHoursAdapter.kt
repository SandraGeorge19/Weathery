package iti.mad42.weathery.home.view

import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.TodayTempHoursCustomCellBinding
import iti.mad42.weathery.model.pojo.CurrentWeather
import iti.mad42.weathery.model.pojo.TodayHoursTemp
import iti.mad42.weathery.model.pojo.Utility
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class TodayTempHoursAdapter(
    var hoursList: List<CurrentWeather>,
    var context: Context?,
) : RecyclerView.Adapter<TodayTempHoursAdapter.ViewHolder>(){

    var languageShared = context?.getSharedPreferences("Language", Context.MODE_PRIVATE)
    var langu = languageShared?.getString(Utility.Language_Key, "en")!!
    var unitsShared = context?.getSharedPreferences("Units", Context.MODE_PRIVATE)
    var unit = unitsShared?.getString(Utility.TEMP_KEY,"metric")!!

    class ViewHolder(val binding: TodayTempHoursCustomCellBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.hourTempStatusIcon
            binding.hourTxt
            binding.gradientColorForTodayHour
            binding.hourTempDegreeTxt
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodayTempHoursAdapter.ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding : TodayTempHoursCustomCellBinding = TodayTempHoursCustomCellBinding.inflate(inflater, parent, false)
        val viewHolder : ViewHolder = ViewHolder(binding)
        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodayTempHoursAdapter.ViewHolder, position: Int) {
        with(holder){
            binding.hourTxt.text = Utility.timeStampToHour(hoursList[position].dt)

            if(langu == "en" && unit == "metric"){
                binding.hourTempDegreeTxt.text = "${hoursList[position].temp.toInt()} ℃"
            }else if(langu == "ar" && unit == "metric"){
                binding.hourTempDegreeTxt.text = Utility.convertNumbersToArabic(hoursList[position].temp.toInt()) + " س°"
            }else if(langu == "en" && unit == "imperial"){
                binding.hourTempDegreeTxt.text = "${hoursList[position].temp.toInt()} ℉"
            }else if(langu == "ar" && unit == "imperial"){
                binding.hourTempDegreeTxt.text = Utility.convertNumbersToArabic(hoursList[position].temp.toInt()) +"ف° "
            }else if(langu == "en" && unit == "standard"){
                binding.hourTempDegreeTxt.text = "${hoursList[position].temp.toInt()} °K"
            }else if(langu == "ar" && unit == "standard"){
                binding.hourTempDegreeTxt.text = Utility.convertNumbersToArabic(hoursList[position].temp.toInt()) +" ك° "
            }

            binding.hourTempStatusIcon.setImageResource(Utility.getWeatherIcon(hoursList[position].weather[0].icon))
            Log.i("san", "onBindViewHolder: ${Utility.timeStampToHourOneNumber(hoursList[position].dt)} and locale : ${LocalDateTime.now().hour.minus(12).toLong()}")
            if(LocalDateTime.now().hour > 12) {
                if (Utility.timeStampToHourOneNumber(hoursList[position].dt) != LocalDateTime.now().hour.minus(12).toString()) {
                    binding.gradientColorForTodayHour.visibility = View.GONE
                }
            }else{
                if (Utility.timeStampToHourOneNumber(hoursList[position].dt) != LocalDateTime.now().hour.toString()) {
                    binding.gradientColorForTodayHour.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return hoursList.size
    }
}