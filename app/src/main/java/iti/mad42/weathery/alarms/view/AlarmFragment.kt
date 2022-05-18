package iti.mad42.weathery.alarms.view

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import iti.mad42.weathery.R
import iti.mad42.weathery.addalarmscreen.view.AddAlarmActivity
import iti.mad42.weathery.alarms.viewmodel.AlarmViewModel
import iti.mad42.weathery.alarms.viewmodel.AlarmViewModelFactory
import iti.mad42.weathery.databinding.FragmentAlarmBinding
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.Repository
import iti.mad42.weathery.model.pojo.TodayHoursTemp
import iti.mad42.weathery.model.pojo.Utility

class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding
    private lateinit var alarmAdapter: AlarmAdapter
    lateinit var alarmVM : AlarmViewModel
    lateinit var alarmVMFactory : AlarmViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAddAlarmBtnListener()
        initAlarmsRecycler()
        initAlarmVMAndFactory()

        getAlarms()
    }

    fun initAlarmsRecycler(){
        binding.alertRecycler
        alarmAdapter = AlarmAdapter(listOf<AlarmPojo>(), context)
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

    fun getAlarms(){
        alarmVM.getAllAlarms()?.observe(viewLifecycleOwner){ alarms ->
            if(alarms.isNotEmpty()){
                alarmAdapter.alarmsList = alarms
                alarmAdapter.notifyDataSetChanged()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setAddAlarmBtnListener(){
        binding.addAlertBtn.setOnClickListener {
            onClickOpenAddAlarm()
        }
    }

    fun checkPermission() : Boolean{
        var overlayShared : SharedPreferences = requireContext().getSharedPreferences("overlay", AppCompatActivity.MODE_PRIVATE)
        Log.e("sandra", "checkPermission: ${overlayShared.getBoolean("overlay", true)}", )
        return overlayShared.getBoolean("overlay", false)
    }

    fun setPermissionGranted(){
        Utility.saveOverlayPermission(requireContext(), "overlay", true)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onClickOpenAddAlarm(){
        checkDrawOverlayPermission()
        if(checkPermission()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkDrawOverlayPermission()
                setPermissionGranted()
            } else {
                val intent = Intent(requireContext(), AddAlarmActivity::class.java)
                startActivity(intent)
            }
        }else {
            val intent = Intent(requireContext(), AddAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("Permission Requires")
                .setMessage(
                            "Please Allow Overlay Permission")
                .setPositiveButton("Ok") { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().packageName)
                    )
                    dialog.dismiss()
                    startActivityForResult(intent, 1)

                }.setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }
}