package iti.mad42.weathery.home.view

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.TodayTempHoursCustomCellBinding
import iti.mad42.weathery.model.pojo.TodayHoursTemp

class TodayTempHoursAdapter(
    var hoursList: List<TodayHoursTemp>,
    var context: Context?,
) : RecyclerView.Adapter<TodayTempHoursAdapter.ViewHolder>(){

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

    override fun onBindViewHolder(holder: TodayTempHoursAdapter.ViewHolder, position: Int) {
        with(holder){
            binding.hourTxt.text = hoursList[position].hour
            binding.hourTempDegreeTxt.text = hoursList[position].degree
            binding.hourTempStatusIcon.setImageResource(R.drawable.clear_sky)
            if(!hoursList[position].hour.equals("02:00 PM")){
                binding.gradientColorForTodayHour.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return hoursList.size
    }
}