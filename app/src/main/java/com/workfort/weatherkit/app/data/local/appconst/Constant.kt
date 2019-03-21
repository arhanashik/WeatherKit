package com.workfort.weatherkit.app.data.local.appconst

object Constant {
    object RequestCode {
        const val LOCATION = 1010
    }

    object Key {

    }

    object PlaceId {
        const val KHULNA = "ChIJHcn1kQ-Q_zkRZXsBlTIv_qM"
        const val DHAKA = "ChIJgWsCh7C4VTcRwgRZ3btjpY8"
        const val BANGLADESH = "ChIJp4vhgO2qrTARa_zhxOAoLQ8"
    }

    object RemoteConfig {
        const val WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val PLACE_API_BASE_URL = "https://maps.googleapis.com/maps/api/place/"

        const val ACCESS_TOKEN = "your-access-token-here"
        const val CONTENT_TYPE = "application/x-www-form-urlencoded"
    }
}