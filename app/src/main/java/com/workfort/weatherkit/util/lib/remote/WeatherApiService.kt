package com.workfort.weatherkit.util.lib.remote

import com.workfort.weatherkit.app.data.local.appconst.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WeatherApiService {
    companion object {
        fun create(): WeatherApiClient {

            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                //.addInterceptor { HeaderInterceptor().intercept(it) }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.RemoteConfig.WEATHER_API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(WeatherApiClient::class.java)
        }
    }
}