package com.workfort.weatherkit.util.lib.remote

import com.workfort.weatherkit.app.data.local.appconst.Const
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("x-access-token",
                    Const.RemoteConfig.ACCESS_TOKEN
                )
                .addHeader("Content-Type",
                    Const.RemoteConfig.CONTENT_TYPE
                )
                .build()
        )
    }
}