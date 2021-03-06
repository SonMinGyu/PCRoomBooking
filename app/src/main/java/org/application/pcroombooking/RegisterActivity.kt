package org.application.pcroombooking

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import org.application.pcroombooking.dto.*
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    lateinit var registerActEmailText: EditText
    lateinit var registerActCryptogramText: EditText
    lateinit var registerActUnivAuthButton: Button
    lateinit var registerActCryptogramTextVerifyButton: Button
    lateinit var registerActUsername: EditText
    lateinit var registerActPasswordText: EditText
    lateinit var registerActPasswordTextConfirm: EditText
    lateinit var registerActRegisterButton: Button
    lateinit var registerActMajorSpinner: Spinner
    lateinit var registerActAdmissionNumberSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val retrofitService: RetrofitService = MasterApplication.retrofitService

        initView(this@RegisterActivity)
        setSpinner(this@RegisterActivity)

        registerActUnivAuthButton.setOnClickListener(View.OnClickListener {
            sendMail(retrofitService, this@RegisterActivity)
        })

        registerActCryptogramTextVerifyButton.setOnClickListener(View.OnClickListener {
            verifyCryptogram(retrofitService, this@RegisterActivity)
        })

        registerActRegisterButton.setOnClickListener(View.OnClickListener {
            register(retrofitService, this@RegisterActivity)
        })
    }

    fun initView(activity: Activity) {
        registerActEmailText = activity.findViewById(R.id.register_activity_email)
        registerActCryptogramText = activity.findViewById(R.id.register_activity_cryptogramText)
        registerActUnivAuthButton = activity.findViewById(R.id.register_activity_univAuth_button)
        registerActCryptogramTextVerifyButton =
            activity.findViewById(R.id.register_activity_cryptogramText_verify_button)
        registerActUsername = activity.findViewById(R.id.register_activity_username)
        registerActPasswordText = activity.findViewById(R.id.register_activity_password)
        registerActPasswordTextConfirm =
            activity.findViewById(R.id.register_activity_passwordConfirm)
        registerActRegisterButton = activity.findViewById(R.id.register_activity_register_button)
        registerActMajorSpinner = activity.findViewById(R.id.register_activity_major_spinner)
        registerActAdmissionNumberSpinner =
            activity.findViewById(R.id.register_activity_admissionNumber_spinner)
    }

    fun setSpinner(activity: Activity) {
        registerActMajorSpinner.adapter = ArrayAdapter.createFromResource(activity,
            R.array.spinner_major,
            android.R.layout.simple_spinner_item)
        registerActAdmissionNumberSpinner.adapter = ArrayAdapter.createFromResource(activity,
            R.array.spinner_admissionNumber,
            android.R.layout.simple_spinner_item)
    }

    fun register(retrofitService: RetrofitService, activity: Activity) {
        val usernameText = getUsernameText()
        val passwordText = getPasswordText()
        val passwordConfirmText = getPasswordConfirmText()

        if (registerActCryptogramText.isEnabled) {
            Toast.makeText(activity, "????????? ????????? ?????? ???????????????. ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }
        if (usernameText == "") {
            Toast.makeText(activity, "????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }
        if (passwordText == "") {
            Toast.makeText(activity, "??????????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }
        if (passwordConfirmText == "") {
            Toast.makeText(activity, "???????????? ????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }
        if (passwordText != passwordConfirmText) {
            Toast.makeText(activity, "??????????????? ???????????? ????????? ????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }
        if (registerActMajorSpinner.selectedItemPosition == 0) {
            Toast.makeText(activity, "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show()
            return
        }
        if (registerActAdmissionNumberSpinner.selectedItemPosition == 0) {
            Toast.makeText(activity, "??????????????? ????????? ?????????.", Toast.LENGTH_SHORT).show()
            return
        }

        // UserRegisterRequest ???????????? ????????????
        val userRegisterRequest =
            UserRegisterRequest(usernameText, getEmailText(), getPasswordText(),
                getAdmissionSpinnerText(), getMajorSpinnerText(), getCryptogramText())
        retrofitService.register(userRegisterRequest)
            .enqueue(object : Callback<UserRegisterResponse> {
                override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                    //todo ????????????

                    Log.d("RegisterActivity", "register onfailure: error by network!!!!!")
                    Log.d("RegisterActivity", t.toString())
                }

                override fun onResponse(
                    call: Call<UserRegisterResponse>,
                    response: Response<UserRegisterResponse>,
                ) {
                    //todo ????????????

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2001) {
                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()

                            // register ????????? activity ??????
                            activity.finish()
                        } else {
                            Log.d("RegisterActivity", "register onfailure: error by server!!!!!")
                            Log.d("RegisterActivity",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("RegisterActivity", "responseCode " + it.responseCode.toString())
                            Log.d("RegisterActivity", "result " + it.result)
                            Log.d("RegisterActivity", "responseMessage" + it.responseMessage)

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    fun verifyCryptogram(retrofitService: RetrofitService, activity: Activity) {
        val cryptogramText = getCryptogramText()

        if (cryptogramText == "") {
            Toast.makeText(activity, "???????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }

        val cryptogramRequest = CryptogramRequest(cryptogramText, getEmailText())
        retrofitService.vefiryCryptogram(cryptogramRequest)
            .enqueue(object : Callback<CryptogramResponse> {
                override fun onFailure(call: Call<CryptogramResponse>, t: Throwable) {
                    //todo ????????????

                    Log.d("RegisterActivity", "cryptogram verify onfailure: error by network!!!!!")
                    Log.d("RegisterActivity", t.toString())
                }

                override fun onResponse(
                    call: Call<CryptogramResponse>,
                    response: Response<CryptogramResponse>,
                ) {
                    //todo ????????????

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2003) {
                            // ?????? ????????? disable??? ??????
                            registerActCryptogramText.isEnabled = false
                            registerActCryptogramTextVerifyButton.isEnabled = false
                            registerActUnivAuthButton.isEnabled = false
                            registerActEmailText.isEnabled = false
                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("RegisterActivity",
                                "cryptogram verify onfailure: error by server!!!!!")
                            Log.d("RegisterActivity",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("RegisterActivity", "responseCode " + it.responseCode.toString())
                            Log.d("RegisterActivity", "result " + it.result)
                            Log.d("RegisterActivity", "responseMessage" + it.responseMessage)

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    fun sendMail(retrofitService: RetrofitService, activity: Activity) {
        val email = getEmailText()

        if (email == "") {
            Toast.makeText(activity, "???????????? ???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(activity, "????????? ????????? ????????? ????????????.\n???????????? ????????? ?????????.", Toast.LENGTH_SHORT).show()
            return
        }
        // ?????? email ?????? ?????????
        if(email.split("@")[1] != "naver.com") {
            Toast.makeText(activity, "????????? ????????? ????????? ?????????.\n?????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }

        val emailSendRequest = EmailSendRequest(email)
        retrofitService.sendMail(emailSendRequest)
            .enqueue(object : Callback<EmailSendResponse> {
                override fun onFailure(call: Call<EmailSendResponse>, t: Throwable) {
                    //todo ????????????

                    Log.d("RegisterActivity", "email send onfailure: error by network!!!!!")
                    Log.d("RegisterActivity", t.toString())
                }

                override fun onResponse(
                    call: Call<EmailSendResponse>,
                    response: Response<EmailSendResponse>,
                ) {
                    //todo ????????????

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {

                        if (it.responseHttpStatus == 200 && it.responseCode == 2002) {
                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("RegisterActivity", "email send onfailure: error by server!!!!!")
                            Log.d("RegisterActivity",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("RegisterActivity", "responseCode " + it.responseCode.toString())
                            Log.d("RegisterActivity", "result " + it.result)
                            Log.d("RegisterActivity", "responseMessage" + it.responseMessage)

                            Toast.makeText(activity,
                                it.responseMessage + " ????????? ?????? ??????????????????.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    fun getEmailText(): String {
        return registerActEmailText.text.toString()
    }

    fun getCryptogramText(): String {
        return registerActCryptogramText.text.toString().trim()
    }

    fun getUsernameText(): String {
        return registerActUsername.text.toString()
    }

    fun getPasswordText(): String {
        return registerActPasswordText.text.toString()
    }

    fun getPasswordConfirmText(): String {
        return registerActPasswordTextConfirm.text.toString()
    }

    fun getMajorSpinnerText(): String {
        return registerActMajorSpinner.selectedItem.toString()
    }

    fun getAdmissionSpinnerText(): String {
        return registerActAdmissionNumberSpinner.selectedItem.toString()
    }

}