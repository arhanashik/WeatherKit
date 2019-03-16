package com.workfort.weatherkit.app.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(@SerializedName("coord") val coordinate: Coordinate,
                                   val list: ArrayList<Forecast>)