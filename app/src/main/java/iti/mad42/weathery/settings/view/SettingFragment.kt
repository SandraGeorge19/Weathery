package iti.mad42.weathery.settings.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.FragmentHomeBinding
import iti.mad42.weathery.databinding.FragmentSettingBinding
import iti.mad42.weathery.model.pojo.LocaleManager
import iti.mad42.weathery.model.pojo.Utility
import java.util.*


class SettingFragment : Fragment() {

    lateinit var binding : FragmentSettingBinding
    lateinit var myIntent : Intent

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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changeLanguage()
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
                    Utility.saveLanguageToSharedPref(requireContext(), Utility.Language_Key, Utility.Language_EN_Value)
                    LocaleManager.setLocale(requireContext())
                    Toast.makeText(requireContext(), "You changed Language to English", Toast.LENGTH_SHORT).show()
                    refreshFragment()
                }
                checkedButtonId == binding.arabicRB.id -> {
                    Utility.saveLanguageToSharedPref(requireContext(), Utility.Language_Key, Utility.Language_AR_Value)
                    LocaleManager.setLocale(requireContext())
                    Toast.makeText(requireContext(), "You changed Language to Arabic", Toast.LENGTH_SHORT).show()
                    refreshFragment()
                }
            }
            //myIntent = Intent(requireActivity(), SettingFragment::class.java)

        }
    }

    private fun changeTemp(){
        binding.tempGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.celsiusRB.id ->{
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.METRIC)
                    Toast.makeText(requireContext(), "You changed Temperature to Celsius", Toast.LENGTH_SHORT).show()
                    refreshFragment()
                }
                checkedButtonId == binding.kelvinRB.id ->{
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.STANDARD)
                    Toast.makeText(requireContext(), "You changed Temperature to Kelvin", Toast.LENGTH_SHORT).show()
                    refreshFragment()
                }
                checkedButtonId == binding.fahrenheitRB.id ->{
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.IMPERIAL)
                    Toast.makeText(requireContext(), "You changed Temperature to Fahrenheit", Toast.LENGTH_SHORT).show()
                    refreshFragment()
                }
            }
        }
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if(isVisibleToUser){
//            fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
//        }
//    }
    private fun refreshFragment(){
        fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
    }

}