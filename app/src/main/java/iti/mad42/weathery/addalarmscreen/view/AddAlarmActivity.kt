package iti.mad42.weathery.addalarmscreen.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import iti.mad42.weathery.addalarmscreen.viewmodel.AddAlarmViewModel
import iti.mad42.weathery.addalarmscreen.viewmodel.AddAlarmViewModelFactory
import iti.mad42.weathery.databinding.ActivityAddAlarmBinding
import iti.mad42.weathery.favourites.viewmodel.FavoritesViewModel
import iti.mad42.weathery.favourites.viewmodel.FavoritesViewModelFactory
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.pojo.AlarmPojo
import iti.mad42.weathery.model.pojo.Repository
import iti.mad42.weathery.model.pojo.Utility
import iti.mad42.weathery.workmanager.MyPeriodicWorkManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AddAlarmActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddAlarmBinding
    lateinit var myAlarm : AlarmPojo
    lateinit var addAlarmVM : AddAlarmViewModel
    lateinit var addAlarmFactory : AddAlarmViewModelFactory
    var alarmHour = 0
    var alarmMinute = 0
    var alarmInterval = 1
    var alarmTitle : String = "7777777777777"
    var startDate : String = ""
    var endDate : String = ""
    var startDateLong : Long = 0
    var endDateLong : Long = 0
    var alarmTime : Long = 0
    var alarmType : String = ""
    var alarmDays : List<String> = listOf()
    var isNotification : Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myAlarm = AlarmPojo(alarmTitle, startDateLong, endDateLong, alarmTime, alarmType, alarmDays, isNotification)
        initUI()
        onClickBack()
        initFavFactoryAndViewModel()
        setAlertTypeAdapter()
        setListeners()
    }

    private fun initFavFactoryAndViewModel(){
        addAlarmFactory = AddAlarmViewModelFactory(Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(this), this))
        addAlarmVM = ViewModelProvider(this,addAlarmFactory).get(AddAlarmViewModel::class.java)
    }
    fun onClickBack(){
        binding.backBtn.setOnClickListener{
            finish()
        }
    }

    fun initUI(){
        binding.alarmTitleEdt
        binding.alarmFromEdit
        binding.alarmToEdit
        binding.alarmTypeSpinner
        binding.alarmSoundGroup
        binding.addAlarmBtn
    }
    fun setAlertTypeAdapter(){
        var alertTypeArrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Utility.ALERT_TYPE_ARRAY)
        binding.alarmTypeSpinner.adapter = alertTypeArrayAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setListeners(){

        binding.alarmFromEdit.editText?.setOnClickListener {
            openDatePicker("from")
        }

        binding.alarmToEdit.editText?.setOnClickListener {
            openDatePicker("to")
            openTimePicker()
        }



        binding.alarmTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                myAlarm.alarmType = Utility.ALERT_TYPE_ARRAY[position]
                alarmType = myAlarm.alarmType
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.alarmSoundGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.notificationRB.id ->{
                    myAlarm.isNotification = true
                    isNotification = myAlarm.isNotification
                }
                checkedButtonId == binding.soundRB.id ->{
                    myAlarm.isNotification = false
                    isNotification = myAlarm.isNotification
                }
            }
        }

        binding.addAlarmBtn.setOnClickListener {
            if(myAlarm.alarmTime > 0){

                myAlarm.alarmTitle = binding.alarmTitleEdt.editText?.text.toString()
                alarmTitle = myAlarm.alarmTitle
                Log.e("Sanfdra", "setListeners: alarm title ${alarmTitle}", )
                myAlarm.alarmDays = addAlarmVM.getDays(startDate, endDate, alarmInterval)
                alarmDays = myAlarm.alarmDays

                myAlarm = AlarmPojo(alarmTitle , startDateLong, endDateLong, alarmTime, alarmType, alarmDays, isNotification)
                addAlarmVM.addAlarm(myAlarm)
                setWorkTimer()
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDatePicker(state : String){
        val calender : Calendar = Calendar.getInstance()
        var now : Long = System.currentTimeMillis() - 1000
        val datePickerDialog = DatePickerDialog(this,
            { viewDatePicker: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                if (state == "from") {
                    startDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    myAlarm.alarmStartDate = Utility.dateToLong(startDate)
                    startDateLong = myAlarm.alarmStartDate
                    binding.alarmFromEdit.editText?.setText(startDate)
                } else {
                    endDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    myAlarm.alarmEndDate = Utility.dateToLong(endDate)
                    endDateLong = myAlarm.alarmEndDate
                    binding.alarmToEdit.editText?.setText(endDate)
                }
            }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH)
        )

        if(state.equals("from")){
            datePickerDialog.datePicker.minDate = now - (1000 * 60 * 60 * 24 * 14)
        } else {
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        }
        datePickerDialog.show()

    }


    fun openTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this,
            { viewTimePicker, hour, minute ->
                alarmHour = hour
                alarmMinute = minute
                Log.e("sandra", "openTimePicker: hour $alarmHour min : $alarmMinute", )
                myAlarm.alarmTime = Utility.timeToMillis(hour, minute)
                alarmTime = myAlarm.alarmTime
                Log.e("sandra", "openTimePicker: alarm Time is:  ${myAlarm.alarmTime}")
                //view.setRefillTime(refillHour.toString() + ":" + refillMinute)
            }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true
        )
        timePickerDialog.show()
    }

    private fun setWorkTimer() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            MyPeriodicWorkManager::class.java, 24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "MyWorkManager", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
        Log.e("Create alarm","setPeriodWorkManger")
    }
}