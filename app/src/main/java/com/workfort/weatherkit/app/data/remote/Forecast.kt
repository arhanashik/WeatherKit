package com.workfort.weatherkit.app.data.remote

import com.google.gson.annotations.SerializedName

data class Forecast (@SerializedName("dt") val date: Long,
                     val main: Main,
                     val weather: ArrayList<Weather>,
                     val wind: Wind)