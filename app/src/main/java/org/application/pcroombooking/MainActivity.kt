package org.application.pcroombooking

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.application.pcroombooking.fragment.ConferenceRoomFragment
import org.application.pcroombooking.fragment.MySeatFragment
import org.application.pcroombooking.fragment.PCRoomFragment
import org.application.pcroombooking.fragment.SettingFragment

class MainActivity : AppCompatActivity() {

    lateinit var mainActBottomNavigationBar: BottomNavigationView
    val pcRoomFragment by lazy { PCRoomFragment() }
    val conferenceRoomFragment by lazy { ConferenceRoomFragment() }
    val mySeatFragment by lazy { MySeatFragment() }
    val settingFragment by lazy { SettingFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", LoginActivity.companionObjectAccessToken)

        initView(this@MainActivity)
        replaceFragment(pcRoomFragment)

        mainActBottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_bar_pcroom -> {
                    replaceFragment(pcRoomFragment)
                }
                R.id.navigation_bar_conferenceRoom -> {
                    replaceFragment(conferenceRoomFragment)
                }
                R.id.navigation_bar_mySeat -> {
                    replaceFragment(mySeatFragment)
                }
                R.id.navigation_bar_setting -> {
                    replaceFragment(settingFragment)
                }
            }
            true
        }
    }

    fun initView(activity: Activity) {
        mainActBottomNavigationBar = activity.findViewById(R.id.main_activity_bottomNavigation)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frameLayout, fragment)
            .commit()
    }


}