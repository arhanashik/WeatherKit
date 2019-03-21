package com.workfort.weatherkit.util.lib.remote

import com.workfort.weatherkit.app.data.local.appconst.Constant
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("x-access-token",
                    Constant.RemoteConfig.ACCESS_TOKEN
                )
                .addHeader("Content-Type",
                    Constant.RemoteConfig.CONTENT_TYPE
                )
                .build()
        )
    }
}