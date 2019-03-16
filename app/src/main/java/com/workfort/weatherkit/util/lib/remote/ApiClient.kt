package com.workfort.weatherkit.util.lib.remote

import com.workfort.weatherkit.app.data.remote.WeatherResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiClient {
    @GET("weather")
    fun getCurrentWeather(
        @QueryMap params: HashMap<String, Any>
    ): Flowable<WeatherResponse>
}