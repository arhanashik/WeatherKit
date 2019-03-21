package com.workfort.weatherkit.util.lib.remote

import com.workfort.weatherkit.app.data.remote.place.PlaceResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PlaceApiClient {
    @GET("findplacefromtext/json")
    fun findPlace(
        @QueryMap params: HashMap<String, Any>
    ): Flowable<PlaceResponse>
}