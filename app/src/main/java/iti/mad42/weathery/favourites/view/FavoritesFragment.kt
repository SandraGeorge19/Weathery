package iti.mad42.weathery.favourites.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.FragmentFavoritesBinding
import iti.mad42.weathery.model.pojo.TodayHoursTemp


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter : FavoriteLocationAdapter
    lateinit var favList : ArrayList<TodayHoursTemp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favList = ArrayList<TodayHoursTemp>()
        favList.add(TodayHoursTemp("02:00 PM", "23 ℃", "Friday"))
        favList.add(TodayHoursTemp("03:00 PM", "24 ℃", "Saturday"))
        favList.add(TodayHoursTemp("04:00 PM", "25 ℃", "Sunday"))
        favList.add(TodayHoursTemp("05:00 PM", "26 ℃", "Monday"))
        favList.add(TodayHoursTemp("06:00 PM", "27 ℃", "Tuesday"))
        favList.add(TodayHoursTemp("07:00 PM", "29 ℃", "Wednesday"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        var view : View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFavRecycler()
    }

    fun initFavRecycler(){
        binding.favRecycler
        favoritesAdapter = FavoriteLocationAdapter(favList, context)
        binding.favRecycler.setHasFixedSize(true)
        binding.favRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = favoritesAdapter
        }
    }

}