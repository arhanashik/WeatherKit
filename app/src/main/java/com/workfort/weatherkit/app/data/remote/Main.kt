package com.workfort.weatherkit.app.data.remote

import com.google.gson.annotations.SerializedName

data class Main(val temp: Float,
                @SerializedName("temp_min") val minTemp: Float,
                @SerializedName("temp_max") val maxTemp: Float,
                val humidity: Int,
                val pressure: Float)