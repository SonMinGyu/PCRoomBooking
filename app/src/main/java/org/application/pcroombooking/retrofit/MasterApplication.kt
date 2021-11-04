package org.application.pcroombooking.retrofit

import android.app.Application
import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

// retrofit 을 application level에 적용
class MasterApplication : Application() {

    lateinit var retrofitService: RetrofitService

    override fun onCreate() {
        super.onCreate()
    }

    fun createRetrofit() {
        // retrofit에 header 추가
        val header = Interceptor {
            val original = it.request()

            if(checkLogin()) {
                val requeset = original.newBuilder()
                    .header("Authorization", "")
                    .build()
                    it.proceed(requeset)
            } else {
                it.proceed(original)
            }


        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    fun checkLogin(): Boolean {
        val sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        val login_token = sp.getString("login_token", "null")
        if(login_token != "null") {
            return true
        } else {
            return false
        }
    }

    fun getUserToken(): String? {
        val sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        val login_token = sp.getString("login_token", "null")

        if(login_token != "null") {
            return null
        } else {
            return login_token
        }
    }
}