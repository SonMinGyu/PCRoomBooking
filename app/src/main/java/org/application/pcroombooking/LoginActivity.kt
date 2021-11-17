package org.application.pcroombooking

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    lateinit var loginActEmailText: TextView
    lateinit var loginActPasswordText: TextView
    lateinit var loginActEmailSaveCheckBox: CheckBox
    lateinit var loginActAutoLoginCheckBox: CheckBox
    lateinit var loginActLoginButton: Button
    lateinit var loginActRegisterButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView(this@LoginActivity)

    }

    fun initView(activity: Activity) {
        loginActEmailText = activity.findViewById(R.id.login_activity_email)
        loginActPasswordText = activity.findViewById(R.id.login_activity_password)
        loginActEmailSaveCheckBox = activity.findViewById(R.id.login_activity_emailSave_checkBox)
        loginActAutoLoginCheckBox = activity.findViewById(R.id.login_activity_autoLogin_checkBox)
        loginActLoginButton = activity.findViewById(R.id.login_activity_login_button)
        loginActRegisterButton = activity.findViewById(R.id.login_activity_register_button)
    }

    fun getEamil() : String {
        return loginActEmailText.text.toString()
    }

    fun getPassword() : String {
        return loginActPasswordText.text.toString()
    }

}