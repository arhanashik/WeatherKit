package com.workfort.weatherkit.app.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherResponse(val name: String,
                           @SerializedName("sys") val common: Common,
                           val weather: ArrayList<Weather>,
                           val main: Main,
                           val wind: Wind)