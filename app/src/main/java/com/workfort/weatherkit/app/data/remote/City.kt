package com.workfort.weatherkit.app.data.remote

import com.google.gson.annotations.SerializedName

data class City (val id: String,
                 val name: String,
                 @SerializedName("coord") val coordinate: Coordinate)