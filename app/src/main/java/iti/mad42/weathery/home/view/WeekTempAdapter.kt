package iti.mad42.weathery.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.AllWeekTempCustomCellBinding
import iti.mad42.weathery.model.pojo.TodayHoursTemp

class WeekTempAdapter(
    var weekList: List<TodayHoursTemp>,
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
            holder.binding.weekDayTxt.text = weekList[position].day
            holder.binding.weekDayDateTxt.text = weekList[position].hour
            holder.binding.weekDayTempDegreeTxt.text = weekList[position].degree
            holder.binding.weekDayTempStatusIcon.setImageResource(R.drawable.clear_sky)
        }
    }

    override fun getItemCount(): Int {
        return weekList.size
    }
}