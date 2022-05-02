package iti.mad42.weathery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import iti.mad42.weathery.alarms.view.AlarmFragment
import iti.mad42.weathery.favourites.view.FavoritesFragment
import iti.mad42.weathery.home.view.HomeFragment
import iti.mad42.weathery.settings.view.SettingFragment

class MainActivity : AppCompatActivity() {
    lateinit var bubbleNavigationLinearView: BubbleNavigationLinearView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //comment
        bubbleNavigationLinearView = findViewById(R.id.bubbleNavigationBar)

        setFragment(HomeFragment())
        bubbleNavigationLinearView.setNavigationChangeListener{ view, position ->
            when(position){
                0 -> setFragment(HomeFragment())
                1 -> setFragment(FavoritesFragment())
                2 -> setFragment(AlarmFragment())
                3 -> setFragment(SettingFragment())
            }
        }


    }

    fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}