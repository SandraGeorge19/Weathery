package iti.mad42.weathery.addalarmscreen.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.ActivityAddAlarmBinding
import iti.mad42.weathery.model.pojo.Utility
import java.util.*

class AddAlarmActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        onClickBack()
        setAlertTypeAdapter()
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
        binding.alarmTypeSpinner
    }

    private fun openDatePicker(state : String){
        val calender : Calendar = Calendar.getInstance()
        var now : Long = System.currentTimeMillis() - 1000
//        val datePickerDialog = DatePickerDialog(this,
//            { viewDatePicker: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
//                if (state == "from") {
//                    startDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
//                    med.setStartDate(dateToLong(startDate))
//                    view.setStartDateTextView(startDate)
//                } else {
//                    endDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
//                    med.setEndDate(dateToLong(endDate))
//                    view.setEndDateTextView(endDate)
//                }
//            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
//        )

    }

}