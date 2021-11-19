package org.application.pcroombooking

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import org.application.pcroombooking.dto.UserLoginRequest
import org.application.pcroombooking.dto.UserLoginResponse
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    companion object {
        lateinit var companionObjectAccessToken: String
    }

    lateinit var loginActEmailText: EditText
    lateinit var loginActPasswordText: EditText
    lateinit var loginActEmailSaveCheckBox: CheckBox
    lateinit var loginActAutoLoginCheckBox: CheckBox
    lateinit var loginActLoginButton: Button
    lateinit var loginActRegisterButton: Button
    lateinit var sharedPref: SharedPreferences
    lateinit var sharedEditor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = this.getPreferences(0)
        sharedEditor = sharedPref.edit()

        val retrofitService: RetrofitService = MasterApplication.retrofitService

        // 저번에 autoLogin 설정하고 로그인했었으면 sharedPreference에 id, pw가 있을것이므로
        // id, pw로 바로 로그인 요청하고 성공하면 mainActivity로 바로 이동시키기
        excuteAutoLogin(retrofitService, this@LoginActivity)

        // view 불러오기
        initView(this@LoginActivity)
        // view에 setting
        getSettings()
        // emailSavedCheck 박스에 체크되어있으면 email 불러와서 setting
        setEmail()

        loginActLoginButton.setOnClickListener(View.OnClickListener {
            login(retrofitService, this@LoginActivity, getEamilText().trim(), getPasswordText().trim())
        })

        loginActRegisterButton.setOnClickListener(View.OnClickListener {
            register(this@LoginActivity)
        })

        loginActEmailSaveCheckBox.setOnClickListener(View.OnClickListener {
//            loginActEmailSaveCheckBox.isChecked = !loginActEmailSaveCheckBox.isChecked
            loginActEmailSaveCheckBox.toggle()
        })

        loginActAutoLoginCheckBox.setOnClickListener(View.OnClickListener {
            loginActAutoLoginCheckBox.toggle()
            if(loginActAutoLoginCheckBox.isChecked) {
                loginActEmailSaveCheckBox.isChecked = true
                loginActEmailSaveCheckBox.isEnabled = !loginActAutoLoginCheckBox.isChecked
            } else {
                loginActEmailSaveCheckBox.isEnabled = !loginActAutoLoginCheckBox.isChecked
            }
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

    fun excuteAutoLogin(retrofitService: RetrofitService, activity: Activity) {
        if(sharedPref.getBoolean("AutoLogin", false)) {
            val sharedPreEmail = sharedPref.getString("Email", "null")
            val sharedPrePassword = sharedPref.getString("Password", "null")

            if (sharedPreEmail != null && sharedPrePassword != null) {
                login(retrofitService, activity, sharedPreEmail, sharedPrePassword)
            } else {
                Toast.makeText(activity, "로그인 정보 불러오기 실패! 다시 로그인을 진행해 주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getSettings() {
        val isSavedEmail: Boolean = sharedPref.getBoolean("SaveEmail", false)
        val isAutoLogin: Boolean = sharedPref.getBoolean("AutoLogin", false)

        loginActEmailSaveCheckBox.isChecked = isSavedEmail
        loginActAutoLoginCheckBox.isChecked = isAutoLogin
    }

    fun setEmail() {
        if(loginActEmailSaveCheckBox.isChecked) {
            val savedEmail = sharedPref.getString("Email", "")
            loginActEmailText.setText(savedEmail)
        }
    }

    // login button 클릭시 작동
    fun saveEmail() {
        // email 저장 checkBox 처리
        if(loginActEmailSaveCheckBox.isChecked) {
            val getEmail: String = loginActEmailText.text.toString()
            sharedEditor.putString("Email", getEmail).apply()
            sharedEditor.putBoolean("SaveEmail", true).apply()
        } else {
            sharedEditor.remove("Email").apply()
            sharedEditor.putBoolean("SaveEmail", false).apply()
        }
    }

    // login button 클릭시 작동
    fun saveAutoLogin() {
        if(loginActAutoLoginCheckBox.isChecked) {
            val getPassword: String = loginActPasswordText.text.toString()
            sharedEditor.putString("Password", getPassword).apply()
            sharedEditor.putBoolean("AutoLogin", true).apply()
        } else {
            sharedEditor.remove("Password").apply()
            sharedEditor.putBoolean("AutoLogin", false).apply()
        }
    }

    fun register(activity: Activity) {
        val intent = Intent(activity, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun login(retrofitService: RetrofitService, activity: Activity, email: String, password: String) {
        val userLoginRequest = UserLoginRequest(email, password)

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

                            // 여기까지 왔으면 신규 로그인
                            // token을 sharedPreference에 저장하지말고
                            // companion object 변수에 저장해서 사용하자
                            val accessToken: String = it.jwtToken

//                            sharedEditor.putString("Access Token", accessToken).apply()

                            val intent = Intent(activity, MainActivity::class.java)
                            intent.apply {
                                // 혹시 모르니 intent로 전달, companion object가 잘 작동하면 지워도 됨
                                this.putExtra("Access Token", accessToken)
                                this.putExtra("login success message", it.responseMessage)
                            }

                            // emailSavedCheck 박스가 true이면 email 저장
                            saveEmail()
                            // autoLoginCheck 박스가 true이면 password 저장
                            // autoLoginCheck 박스가 true면 emailSavedCheck 박스는 무조건 true로 설정되므로 email 자동저장
                            saveAutoLogin()

                            companionObjectAccessToken = accessToken
                            startActivity(intent)
                            activity.finish()

                        } else {
                            Log.d("LoginActivity", "login onfailure: error by server!!!!!")
                            Log.d("LoginActivity", "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("LoginActivity", "responseCode " + it.responseCode.toString())
                            Log.d("LoginActivity", "result " + it.result)
                            Log.d("LoginActivity", "responseMessage" + it.responseMessage)

                            Toast.makeText(activity,
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