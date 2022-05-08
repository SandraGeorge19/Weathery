package iti.mad42.weathery.alarms.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.mad42.weathery.addalarmscreen.view.AddAlarmActivity
import iti.mad42.weathery.alarms.viewmodel.AlarmViewModel
import iti.mad42.weathery.alarms.viewmodel.AlarmViewModelFactory
import iti.mad42.weathery.databinding.FragmentAlarmBinding
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.pojo.Repository
import iti.mad42.weathery.model.pojo.TodayHoursTemp

class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding
    private lateinit var alarmAdapter: AlarmAdapter
    lateinit var alarmVM : AlarmViewModel
    lateinit var alarmVMFactory : AlarmViewModelFactory
    lateinit var alarmsList : ArrayList<TodayHoursTemp>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmsList = ArrayList<TodayHoursTemp>()
        alarmsList.add(TodayHoursTemp("02:00 PM", "23 ℃", "Friday"))
        alarmsList.add(TodayHoursTemp("03:00 PM", "24 ℃", "Saturday"))
        alarmsList.add(TodayHoursTemp("04:00 PM", "25 ℃", "Sunday"))
        alarmsList.add(TodayHoursTemp("05:00 PM", "26 ℃", "Monday"))
        alarmsList.add(TodayHoursTemp("06:00 PM", "27 ℃", "Tuesday"))
        alarmsList.add(TodayHoursTemp("07:00 PM", "29 ℃", "Wednesday"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        var view : View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAlertBtn.setOnClickListener { startActivity(Intent(context, AddAlarmActivity::class.java)) }

        initAlarmsRecycler()
        initAlarmVMAndFactory()
    }

    fun initAlarmsRecycler(){
        binding.alertRecycler
        alarmAdapter = AlarmAdapter(alarmsList, context)
        binding.alertRecycler.setHasFixedSize(true)
        binding.alertRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = alarmAdapter
        }
    }

    fun initAlarmVMAndFactory(){
        alarmVMFactory = AlarmViewModelFactory(
            Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(requireContext()), requireContext())
        )
        alarmVM = ViewModelProvider(this, alarmVMFactory).get(AlarmViewModel::class.java)
    }
}