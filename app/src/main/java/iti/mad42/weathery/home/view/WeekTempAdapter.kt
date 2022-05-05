package iti.mad42.weathery.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.AllWeekTempCustomCellBinding
import iti.mad42.weathery.model.pojo.DailyWeather
import iti.mad42.weathery.model.pojo.TodayHoursTemp
import iti.mad42.weathery.model.pojo.Utility

class WeekTempAdapter(
    var weekList: List<DailyWeather>,
    var context: Context?
) : RecyclerView.Adapter<WeekTempAdapter.ViewHolder>(){

    class ViewHolder(val binding: AllWeekTempCustomCellBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.weekDayTxt
            binding.weekDayDateTxt
            binding.weekDayTempDegreeTxt
            binding.weekDayTempStatusIcon
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekTempAdapter.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding : AllWeekTempCustomCellBinding = AllWeekTempCustomCellBinding.inflate(inflater, parent, false)
        val viewHolder  : ViewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: WeekTempAdapter.ViewHolder, position: Int) {
        with(holder){
            holder.binding.weekDayTxt.text = Utility.timeStampToDay(weekList[position].dt)
            Log.i("sandra", "onBindViewHolder: ${Utility.timeStampToDay(weekList[position].dt)}")
            holder.binding.weekDayDateTxt.text = Utility.timeStampToDate(weekList[position].dt)
            holder.binding.weekDayTempDegreeTxt.text = "${weekList[position].temp.max.toInt()} / ${weekList[position].temp.min.toInt()} ℃"
            holder.binding.weekDayTempStatusIcon.setImageResource(R.drawable.clear_sky)
        }
    }

    override fun getItemCount(): Int {
        return weekList.size
    }
}