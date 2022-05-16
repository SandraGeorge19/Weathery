package iti.mad42.weathery.favourites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.R
import iti.mad42.weathery.broadcast.NetworkChangeReceiver
import iti.mad42.weathery.databinding.FavoiteScreenCustomCellBinding
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.TodayHoursTemp

class FavoriteLocationAdapter(
    var favLocationList : List<FavoriteWeather>,
    var context: Context?,
    var listener : OnClickFavPlaceListener
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
            holder.binding.favLocationNameTxt.text = favLocationList[position].favLocationName
            holder.binding.favLocationStatusIcon.setImageResource(R.drawable.clear_sky)
            holder.binding.onClickFavCell.setOnClickListener { favPlace ->
                if(NetworkChangeReceiver.isOnline){
                    listener.onClickFavPlace(favLocationList[position])
                }else{
                    Toast.makeText(context, "There is no internet, you can't the this place's weather details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return favLocationList.size
    }
}