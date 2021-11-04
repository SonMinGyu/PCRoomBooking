package org.application.pcroombooking

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    lateinit var emailText: TextView
    lateinit var passwordText: TextView
    lateinit var emailSaveCheckBox: CheckBox
    lateinit var autoLoginCheckBox: CheckBox
    lateinit var loginButton: Button
    lateinit var registerButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView(this@LoginActivity)

    }

    fun initView(activity: Activity) {
        emailText = activity.findViewById(R.id.login_activity_email)
        passwordText = activity.findViewById(R.id.login_activity_password)
        emailSaveCheckBox = activity.findViewById(R.id.login_activity_emailSave_checkBox)
        autoLoginCheckBox = activity.findViewById(R.id.login_activity_autoLogin_checkBox)
        loginButton = activity.findViewById(R.id.login_activity_login_button)
        registerButton = activity.findViewById(R.id.login_activity_register_button)
    }

    fun getEamil() : String {
        return emailText.text.toString()
    }

    fun getPassword() : String {
        return passwordText.text.toString()
    }

}