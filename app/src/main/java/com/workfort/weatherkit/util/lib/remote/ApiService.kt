package com.workfort.weatherkit.util.lib.remote

import com.workfort.weatherkit.app.data.local.appconst.Const
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {
    companion object {
        fun create(): ApiClient {

            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                //.addInterceptor { HeaderInterceptor().intercept(it) }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Const.RemoteConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiClient::class.java)
        }
    }
}