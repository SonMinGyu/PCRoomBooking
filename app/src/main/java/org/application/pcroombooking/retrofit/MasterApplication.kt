package org.application.pcroombooking.retrofit

import android.app.Application
import android.content.Context
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

// retrofit 을 application level에 적용
class MasterApplication : Application() {

    companion object {
        lateinit var retrofitService: RetrofitService
    }

    override fun onCreate() {
        super.onCreate()

        createRetrofit()
    }

    fun createRetrofit() {
        // sharedPreference에 token 이 있으면 header에 추가
        // retrofit에 header 추가
        val header = Interceptor {
            val original = it.request()

            if (checkLogin()) {
                val token: String = getUserToken()
                val requeset = original.newBuilder()
                    .header("Authorization", token)
                    .build()
                it.proceed(requeset)
            } else {
                it.proceed(original)
            }
        }

        val gson = Gson().newBuilder()
            .setLenient()
            .create()

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    fun checkLogin(): Boolean {
        val sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        val login_token = sp.getString("login_token", "null")
        if (login_token != "null") {
            return true
        } else {
            return false
        }
    }

    fun getUserToken(): String {
        val sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        val login_token = sp.getString("login_token", "null")

        if (login_token != "null") {
            return "null"
        } else {
            return login_token
        }
    }
}