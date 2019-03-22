package com.workfort.weatherkit.app.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(@SerializedName("cnt") val count: Int,
                                   val city: City,
                                   val list: ArrayList<Forecast>)