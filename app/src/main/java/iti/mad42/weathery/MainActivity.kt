package iti.mad42.weathery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import iti.mad42.weathery.alarms.view.AlarmFragment
import iti.mad42.weathery.favourites.view.FavoritesFragment
import iti.mad42.weathery.home.view.HomeFragment
import iti.mad42.weathery.model.pojo.LocaleManager
import iti.mad42.weathery.model.pojo.Utility
import iti.mad42.weathery.settings.view.SettingFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var bubbleNavigationLinearView: BottomNavigationView

    private var onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            var fragment: Fragment = HomeFragment()
            when (id) {
                R.id.homeToggle -> {
                    fragment = HomeFragment()
                }
                R.id.favToggle -> {
                    fragment = FavoritesFragment()
                }
                R.id.notificationsToggle -> {
                    fragment = AlarmFragment()
                }
                R.id.settingsToggle -> {
                    fragment = SettingFragment()
                }
            }
            setFragment(fragment)
            true
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //comment
        bubbleNavigationLinearView = findViewById(R.id.bubbleNavigationBar)
        var sharedPreferences : SharedPreferences = getSharedPreferences("Language", Context.MODE_PRIVATE)

        setFragment(HomeFragment())
        setLocale(sharedPreferences.getString(Utility.Language_Key, "en")!!)
        bubbleNavigationLinearView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bubbleNavigationLinearView.setSelectedItemId(R.id.homeToggle)
        bubbleNavigationLinearView.getMenu().clear();
        bubbleNavigationLinearView.inflateMenu(R.menu.bottom_nav_menu);

    }

    private fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
    }

    fun setFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer,fragment)
            commit()
        }
}