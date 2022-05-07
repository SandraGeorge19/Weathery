package iti.mad42.weathery.addalarmscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.ActivityAddAlarmBinding

class AddAlarmActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickBack()
    }
    fun onClickBack(){
        binding.backBtn.setOnClickListener{
            finish()
        }
    }
}