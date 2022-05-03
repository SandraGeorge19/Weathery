package iti.mad42.weathery.favourites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.FavoiteScreenCustomCellBinding
import iti.mad42.weathery.model.pojo.TodayHoursTemp

class FavoriteLocationAdapter(
    var favLocationList : List<TodayHoursTemp>,
    var context: Context?
) : RecyclerView.Adapter<FavoriteLocationAdapter.ViewHolder>(){

    class ViewHolder(val binding: FavoiteScreenCustomCellBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.favLocationNameTxt
            binding.favLocationStatusIcon
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteLocationAdapter.ViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        val binding : FavoiteScreenCustomCellBinding = FavoiteScreenCustomCellBinding.inflate(inflater, parent, false)
        val viewHolder : ViewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: FavoriteLocationAdapter.ViewHolder, position: Int) {
        with(holder){
            holder.binding.favLocationNameTxt.text = favLocationList[position].day
            holder.binding.favLocationStatusIcon.setImageResource(R.drawable.clear_sky)
        }
    }

    override fun getItemCount(): Int {
        return favLocationList.size
    }
}