package org.application.pcroombooking

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import org.application.pcroombooking.dto.UserLoginRequest
import org.application.pcroombooking.dto.UserLoginResponse
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val retrofitService: RetrofitService = MasterApplication.retrofitService

        initView(this@LoginActivity)

        loginActLoginButton.setOnClickListener(View.OnClickListener {
            login(retrofitService, this@LoginActivity)
        })

    }

    fun initView(activity: Activity) {
        loginActEmailText = activity.findViewById(R.id.login_activity_email)
        loginActPasswordText = activity.findViewById(R.id.login_activity_password)
        loginActEmailSaveCheckBox = activity.findViewById(R.id.login_activity_emailSave_checkBox)
        loginActAutoLoginCheckBox = activity.findViewById(R.id.login_activity_autoLogin_checkBox)
        loginActLoginButton = activity.findViewById(R.id.login_activity_login_button)
        loginActRegisterButton = activity.findViewById(R.id.login_activity_register_button)
    }

    fun register(activity: Activity) {
        val intent: Intent = Intent(activity, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun login(retrofitService: RetrofitService, activity: Activity) {
        val userLoginRequest: UserLoginRequest = UserLoginRequest(getEamilText().trim(), getPasswordText().trim())

        retrofitService.login(userLoginRequest)
            .enqueue(object : Callback<UserLoginResponse> {
                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("LoginActivity", "login onfailure: error by network!!!!!")
                    Log.d("LoginActivity", t.toString())
                }

                override fun onResponse(
                    call: Call<UserLoginResponse>,
                    response: Response<UserLoginResponse>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2000) {
                            // mainActivity로 intent 이동, intent에 it.responseMessage 넣어보내고 toast 메세지 띄워주기

                            // token을 sharedPreference에 저장
                            val jwtToken: String = it.jwtToken
                            val pref = activity.getPreferences(0)
                            val editor = pref.edit()

                            editor.putString("Access Token", jwtToken).apply()

                            val intent: Intent = Intent(activity, MainActivity::class.java)
                            intent.apply {
                                this.putExtra("Access Token", jwtToken)
                                this.putExtra("login success message", it.responseMessage)
                            }
                            startActivity(intent)
                            activity.finish()

                        } else {
                            Log.d("LoginActivity", "login onfailure: error by server!!!!!")
                            Log.d("LoginActivity", "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("LoginActivity", "responseCode " + it.responseCode.toString())
                            Log.d("LoginActivity", "result " + it.result)
                            Log.d("LoginActivity", "responseMessage" + it.responseMessage)

                            Toast.makeText(this@LoginActivity,
                                it.responseMessage + " 다시 시도해 주세요.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    fun getEamilText() : String {
        return loginActEmailText.text.toString()
    }

    fun getPasswordText() : String {
        return loginActPasswordText.text.toString()
    }

}