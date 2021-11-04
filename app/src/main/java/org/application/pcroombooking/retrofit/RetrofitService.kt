package org.application.pcroombooking.retrofit

import android.net.http.HttpResponseCache
import org.application.pcroombooking.dto.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {

    @GET("/api/login")
    fun login(): Call<User>

    @POST("/api/register")
    fun register(): Call<HttpResponseCache>

}