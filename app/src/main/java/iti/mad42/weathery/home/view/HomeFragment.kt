package iti.mad42.weathery.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.databinding.FragmentHomeBinding
import iti.mad42.weathery.model.pojo.TodayHoursTemp


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var todayHoursAdapter : TodayTempHoursAdapter
    private lateinit var weekTempAdapter: WeekTempAdapter
    lateinit var hoursList : ArrayList<TodayHoursTemp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hoursList = ArrayList<TodayHoursTemp>()
        hoursList.add(TodayHoursTemp("02:00 PM", "23 ℃", "Friday"))
        hoursList.add(TodayHoursTemp("03:00 PM", "24 ℃", "Saturday"))
        hoursList.add(TodayHoursTemp("04:00 PM", "25 ℃", "Sunday"))
        hoursList.add(TodayHoursTemp("05:00 PM", "26 ℃", "Monday"))
        hoursList.add(TodayHoursTemp("06:00 PM", "27 ℃", "Tuesday"))
        hoursList.add(TodayHoursTemp("07:00 PM", "29 ℃", "Wednesday"))
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
        initHoursRecycler()
        initWeekRecycler()

    }

    fun initHoursRecycler(){
        binding.todayTempRecycler
        todayHoursAdapter = TodayTempHoursAdapter(hoursList, context)
        binding.todayTempRecycler.setHasFixedSize(true)
        binding.todayTempRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            this.adapter = todayHoursAdapter
        }
    }

    fun initWeekRecycler(){
        binding.allWeekTempRecycler
        weekTempAdapter = WeekTempAdapter(hoursList, context)
        binding.allWeekTempRecycler.setHasFixedSize(true)
        binding.allWeekTempRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = weekTempAdapter
        }
    }

}