package iti.mad42.weathery.alarms.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.databinding.AlarmCustomCellBinding
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.TodayHoursTemp

class AlarmAdapter(
    var alarmsList: List<AlarmPojo>,
    var context: Context?
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>(){

    class ViewHolder(val binding: AlarmCustomCellBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.alarmTitleCell
            binding.alarmTypeCell
            binding.alarmSwitch
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmAdapter.ViewHolder {
        var inflater : LayoutInflater = LayoutInflater.from(parent.context)
        var binding: AlarmCustomCellBinding = AlarmCustomCellBinding.inflate(inflater, parent, false)
        var viewHolder : ViewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: AlarmAdapter.ViewHolder, position: Int) {
        with(holder){
            holder.binding.alarmTitleCell.text = alarmsList[position].alarmTitle
            holder.binding.alarmTypeCell.text = alarmsList[position].alarmType
            holder.binding.alarmSwitch.setOnCheckedChangeListener{ _, isChecked ->
                if(isChecked){
                    alarmsList[position].isNotification = true
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show()
                }else{
                    alarmsList[position].isNotification = false
                    Toast.makeText(context, "NOTTTTT Checked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return alarmsList.size
    }
}