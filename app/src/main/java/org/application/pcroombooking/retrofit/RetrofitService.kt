package org.application.pcroombooking.retrofit

import android.net.http.HttpResponseCache
import org.application.pcroombooking.dto.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {

//    @GET("/api/login")
//    fun login(): Call<User>
//
//    @POST("/api/register")
//    fun register(): Call<HttpResponseCache>

    @POST("user/register/mail")
    fun sendMail(@Body body: EmailSendRequest): Call<EmailSendResponse>

    @POST("user/register/cryptogram")
    fun vefiryCryptogram(@Body body: CryptogramRequest): Call<CryptogramResponse>

    @POST("user/register")
    fun register(@Body body: UserRegisterRequest): Call<UserRegisterResponse>

    @GET("pcroom")
    fun getPCRoomList(): Call<PCRoomResponse>



    @POST("user/login")
    fun login(@Body body: UserLoginRequest): Call<UserLoginResponse>

}