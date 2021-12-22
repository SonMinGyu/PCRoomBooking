package org.application.pcroombooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.application.pcroombooking.fragment.ConferenceRoomFragment
import org.application.pcroombooking.fragment.MySeatFragment
import org.application.pcroombooking.fragment.PCRoomFragment
import org.application.pcroombooking.fragment.SettingFragment

class MainActivity : AppCompatActivity() {

    lateinit var mainActBottomNavigationBar: BottomNavigationView
    lateinit var mainActAdminMenuButton: ImageButton
    lateinit var mainActQRCodeButton: ImageButton
    val pcRoomFragment by lazy { PCRoomFragment() }
    val conferenceRoomFragment by lazy { ConferenceRoomFragment() }
    val mySeatFragment by lazy { MySeatFragment() }
    val settingFragment by lazy { SettingFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Log.d("MainActivity", LoginActivity.companionObjectAccessToken)

        initView()
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

        mainActAdminMenuButton.setOnClickListener {
            var adminPopUpMenu = PopupMenu(applicationContext, it)
            menuInflater.inflate(R.menu.admin_pop_up_menu, adminPopUpMenu.menu)
            adminPopUpMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.admin_pop_up_menu_manage_pcroom -> {
                        Log.d("MainActivity", "pcroom manage clicked")
                        val intent = Intent(this, AdminPCRoomActivity::class.java)
                            .apply {
                                Log.d("MainActivity", "open adminPCRoomActivity 실행")
                            }
                        startActivity(intent)
//                        return@setOnMenuItemClickListener true
                    }
                    R.id.admin_pop_up_menu_manage_conferenceroom -> {
                        Log.d("MainActivity", "conferenceRoom manage clicked")
//                        return@setOnMenuItemClickListener true
                    } else -> {
//                        return@setOnMenuItemClickListener false
                    }
                }
                true
            }
            adminPopUpMenu.show()
        }

        mainActQRCodeButton.setOnClickListener {
            scanQRCode()
        }

    }

    fun initView() {
        mainActBottomNavigationBar = findViewById(R.id.main_activity_bottomNavigation)
        mainActAdminMenuButton = findViewById(R.id.main_activity_adminMenu_button)
        mainActQRCodeButton = findViewById(R.id.main_activity_QRCode_button)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frameLayout, fragment)
            .commit()
    }

    fun scanQRCode(){
        val integrator = IntentIntegrator(this)
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(true)
        integrator.setPrompt("QR코드를 찍어주세요.")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            if (result.contents == null) {
                Log.e("this", "잘못된 QR코드입니다.")
            } else {
                Log.e("this", result.contents.toString())
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}