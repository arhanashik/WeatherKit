package com.workfort.weatherkit.app.data.remote

import com.google.gson.annotations.SerializedName

data class Coordinate (val lat: Double,
                       @SerializedName("lon") val lng: Double)