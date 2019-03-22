package com.workfort.weatherkit.util.helper

import com.workfort.weatherkit.R

class WeatherKitUtil {
    fun getWeatherIcon(weather: String): Int {
        return when(weather.toLowerCase()) {
            "clear" -> R.drawable.ic_sun
            "rain" -> R.drawable.ic_rain
            "cloud", "clouds", "haze" -> R.drawable.ic_cloud
            else -> R.drawable.ic_partly_cloudy
        }
    }
}