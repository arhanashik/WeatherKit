package com.workfort.weatherkit.app.data.remote

import com.google.gson.annotations.SerializedName

data class Forecast (@SerializedName("dt") val date: Long,
                val weather: ArrayList<Weather>,
                val main: Main,
                val wind: Wind)