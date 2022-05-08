package iti.mad42.weathery.addalarmscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.ActivityAddAlarmBinding
import iti.mad42.weathery.model.pojo.Utility

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
    }

}