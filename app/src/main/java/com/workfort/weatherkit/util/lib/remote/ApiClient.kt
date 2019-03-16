package com.workfort.weatherkit.util.lib.remote

import com.workfort.weatherkit.app.data.remote.CurrentWeatherResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiClient {
    @GET("weather")
    fun getCurrentWeather(
        @QueryMap params: HashMap<String, Any>
    ): Flowable<CurrentWeatherResponse>

    @GET("forecast")
    fun get5DayWeather(
        @QueryMap params: HashMap<String, Any>
    ): Flowable<CurrentWeatherResponse>
}