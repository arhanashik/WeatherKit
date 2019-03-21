package com.workfort.weatherkit.app.data.remote.place

import com.google.gson.annotations.SerializedName

data class Candidate (val name: String,
                      @SerializedName("place_id") val placeId: String)