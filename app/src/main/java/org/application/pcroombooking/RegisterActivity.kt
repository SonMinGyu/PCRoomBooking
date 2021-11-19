package org.application.pcroombooking

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.application.pcroombooking.dto.EmailSendRequest
import org.application.pcroombooking.dto.EmailSendResponse
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var registerActEmailText: EditText
    lateinit var registerActUnivAuthButton: Button
    lateinit var registerActCryptogramAuthButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val retrofitService: RetrofitService = MasterApplication.retrofitService

        initView(this@RegisterActivity)

        registerActUnivAuthButton.setOnClickListener(View.OnClickListener {
            sendMail(retrofitService)
        })

        registerActCryptogramAuthButton.setOnClickListener(View.OnClickListener {
            retrofitTest(retrofitService)
        })


    }

    fun initView(activity: Activity) {
        registerActEmailText = activity.findViewById(R.id.register_activity_email)
        registerActUnivAuthButton = activity.findViewById(R.id.register_activity_univAuth_button)
        registerActCryptogramAuthButton = activity.findViewById(R.id.register_activity_textAuth_button)
    }

    fun sendMail(retrofitService: RetrofitService) {
        val email = getEmailText()
        if (email == "") {
            Toast.makeText(this, "이메일을 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
        } else {
            val emailSendRequest: EmailSendRequest = EmailSendRequest(email)
            retrofitService.sendMail(emailSendRequest)
                .enqueue(object : Callback<EmailSendResponse> {
                    override fun onFailure(call: Call<EmailSendResponse>, t: Throwable) {
                        //todo 실패처리

                        Log.d("RegisterActivity", "email send onfailure: error by network!!!!!")
                        Log.d("RegisterActivity", t.toString())
                    }

                    override fun onResponse(
                        call: Call<EmailSendResponse>,
                        response: Response<EmailSendResponse>,
                    ) {
                        //todo 성공처리

                        if (response.isSuccessful.not()) {
                            return
                        }

                        response.body()?.let {

                            if (it.responseHttpStatus == 200 && it.responseCode == 2002) {
                                Toast.makeText(this@RegisterActivity,
                                    it.responseMessage,
                                    Toast.LENGTH_SHORT).show()
                            } else {
                                Log.d("RegisterActivity", "email send onfailure: error by server!!!!!")
                                Log.d("RegisterActivity", "httpStatus " + it.responseHttpStatus.toString())
                                Log.d("RegisterActivity", "responseCode " + it.responseCode.toString())
                                Log.d("RegisterActivity", "result " + it.result)
                                Log.d("RegisterActivity", "responseMessage" + it.responseMessage)

                                Toast.makeText(this@RegisterActivity,
                                    it.responseMessage + " 인증을 다시 시도해주세요.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
        }
    }

    fun getEmailText(): String {
        return registerActEmailText.text.toString()
    }

    fun retrofitTest(retrofitService: RetrofitService) {
        retrofitService.test()
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    //todo 실패처리

                    Log.d("RegisterActivity", "fail message!!!!11")
                    Log.d("RegisterActivity", t.toString())
                    Log.d("RegisterActivity", t.stackTraceToString())
                }

                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let {
                        //body가 있다면 그안에는 bestSellerDto가 들어있을것
                        Log.d("RegisterActivity", it)
                    }
                }
            })
    }
}