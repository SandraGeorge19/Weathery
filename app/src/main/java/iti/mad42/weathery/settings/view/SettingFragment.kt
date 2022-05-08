package iti.mad42.weathery.settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.FragmentHomeBinding
import iti.mad42.weathery.databinding.FragmentSettingBinding
import iti.mad42.weathery.model.pojo.Utility


class SettingFragment : Fragment() {

    lateinit var binding : FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        var view : View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        changeLanguage()
        changeTemp()
    }

    fun initUI(){
        binding.locationGroup
        binding.languageGroup
        binding.tempGroup
        binding.windSpeedGroup
        binding.notificationGroup

    }

    private fun changeLanguage(){
        binding.languageGroup.setOnCheckedChangeListener {radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.englishRB.id -> {
                    Utility.saveLanguageToSharedPref(requireContext(), "Lang", "en")
                    Toast.makeText(requireContext(), "You changed Language to English", Toast.LENGTH_SHORT).show()
                }
                checkedButtonId == binding.arabicRB.id -> {
                    Utility.saveLanguageToSharedPref(requireContext(), "Lang", "ar")
                    Toast.makeText(requireContext(), "You changed Language to Arabic", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun changeTemp(){
        binding.tempGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.celsiusRB.id ->{
                    Utility.saveTempToSharedPref(requireContext(), "Temp", "metric")
                    Toast.makeText(requireContext(), "You changed Temperature to Celsius", Toast.LENGTH_SHORT).show()
                }
                checkedButtonId == binding.kelvinRB.id ->{
                    Utility.saveTempToSharedPref(requireContext(), "Temp", "standard")
                    Toast.makeText(requireContext(), "You changed Temperature to Kelvin", Toast.LENGTH_SHORT).show()
                }
                checkedButtonId == binding.fahrenheitRB.id ->{
                    Utility.saveTempToSharedPref(requireContext(), "Temp", "imperial")
                    Toast.makeText(requireContext(), "You changed Temperature to Fahrenheit", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}