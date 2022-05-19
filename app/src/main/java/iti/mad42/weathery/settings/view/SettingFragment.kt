package iti.mad42.weathery.settings.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import iti.mad42.weathery.MainActivity
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.FragmentHomeBinding
import iti.mad42.weathery.databinding.FragmentSettingBinding
import iti.mad42.weathery.favourites.view.MapActivity
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
        changeMap()
        changeLanguage()
        changeTemp()
        changeWindSpeed()
        changeNotification()
    }


    fun initUI(){
        binding.locationGroup
        binding.languageGroup
        binding.tempGroup
        binding.windSpeedGroup
        binding.notificationGroup

    }

    private fun changeMap(){
        binding.locationGroup.setOnCheckedChangeListener{ radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.gpsRB.id -> {
                    Utility.saveIsMapSharedPref(requireContext(), "isMap", false)
                    Toast.makeText(requireContext(), getString(R.string.change_to_gps_txt), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
                }
                checkedButtonId == binding.mapRB.id -> {
                    Utility.saveIsMapSharedPref(requireContext(), "isMap", true)
                    goToMapActivity()
                    Toast.makeText(requireContext(), getString(R.string.change_to_map_txt), Toast.LENGTH_SHORT).show()
                    refreshFragment()

                }
            }
        }
    }
    fun goToMapActivity(){
        Utility.saveIsMapSharedPref(requireContext(), "isMap", true)
        var mapIntent : Intent = Intent(requireActivity(), MapActivity::class.java)
        startActivity(mapIntent)
    }

    fun goToMainScreen(){
        startActivity(Intent( requireActivity(), MainActivity::class.java))

    }

    private fun changeLanguage(){
        binding.languageGroup.setOnCheckedChangeListener {radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.englishRB.id -> {
                    Log.e("sandra", "changeLanguage: en", )
                    Utility.saveLanguageToSharedPref(requireContext(), Utility.Language_Key, Utility.Language_EN_Value)
                    LocaleManager.setLocale(requireContext())
                    Toast.makeText(requireContext(), getString(R.string.change_to_en_txt), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
                }
                checkedButtonId == binding.arabicRB.id -> {
                    Log.e("sandra", "changeLanguage: ar", )
                    Utility.saveLanguageToSharedPref(requireContext(), Utility.Language_Key, Utility.Language_AR_Value)
                    LocaleManager.setLocale(requireContext())
                    Toast.makeText(requireContext(), getString(R.string.change_to_ar_txt), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
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
                    Toast.makeText(requireContext(), getString(R.string.change_to_cel_txt), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
                }
                checkedButtonId == binding.kelvinRB.id ->{
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.STANDARD)
                    Toast.makeText(requireContext(), getString(R.string.change_to_kelvin_txt), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
                }
                checkedButtonId == binding.fahrenheitRB.id ->{
                    Utility.saveTempToSharedPref(requireContext(), Utility.TEMP_KEY, Utility.IMPERIAL)
                    Toast.makeText(requireContext(), getString(R.string.change_to_feh_txt), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
                }
            }
        }
    }

    private fun changeWindSpeed(){
        binding.windSpeedGroup.setOnCheckedChangeListener {radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.meterToSecRB.id -> {
                    Toast.makeText(requireContext(), getString(R.string.change_wind_txt), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
                }
                checkedButtonId == binding.milesToHourRB.id -> {
                    Toast.makeText(requireContext(), getString(R.string.change_wind_to_millis), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
                }
            }
        }
    }
    private fun changeNotification(){
        binding.notificationGroup.setOnCheckedChangeListener {radioGroup, checkedButtonId ->
            when{
                checkedButtonId == binding.enabledRB.id -> {
                    Toast.makeText(requireContext(), getString(R.string.enable_noti_msg), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
                }
                checkedButtonId == binding.disabledRB.id -> {
                    Toast.makeText(requireContext(), getString(R.string.disable_noti_msg), Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    goToMainScreen()
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