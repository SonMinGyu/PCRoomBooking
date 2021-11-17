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
            val email: String = registerActEmailText.text.toString()
            if (email == "") {
                Toast.makeText(this, "이메일을 입력하지 않으셨습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val emailSendRequest: EmailSendRequest = EmailSendRequest(email)
                retrofitService.sendMail(emailSendRequest)
                    .enqueue(object : Callback<EmailSendResponse> {
                        override fun onFailure(call: Call<EmailSendResponse>, t: Throwable) {
                            //todo 실패처리

                            Log.d("RegisterActivity", "fail message!!!!11")
                            Log.d("RegisterActivity", t.toString())
                            Log.d("RegisterActivity", t.stackTraceToString())
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
                                //body가 있다면 그안에는 bestSellerDto가 들어있을것
                                Log.d("RegisterActivity", it.toString())

                                if (it.resultCode == 200) {
                                    Toast.makeText(this@RegisterActivity,
                                        it.message.toString(),
                                        Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@RegisterActivity,
                                        "실패!!!!!",
                                        Toast.LENGTH_SHORT).show()
                                    Log.d("RegisterActivity", it.resultCode.toString())
                                    Log.d("RegisterActivity", it.result)
                                    Log.d("RegisterActivity", it.message)
                                }
                            }
                        }
                    })
            }
        })

        registerActCryptogramAuthButton.setOnClickListener(View.OnClickListener {
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
        })


    }

    fun initView(activity: Activity) {
        registerActEmailText = activity.findViewById(R.id.register_activity_email)
        registerActUnivAuthButton = activity.findViewById(R.id.register_activity_univAuth_button)
        registerActCryptogramAuthButton = activity.findViewById(R.id.register_activity_textAuth_button)
    }


}