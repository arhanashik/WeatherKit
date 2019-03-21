package com.workfort.weatherkit.app.data.remote.place

data class PlaceResponse (val candidates: ArrayList<Candidate>,
                          val status: String)