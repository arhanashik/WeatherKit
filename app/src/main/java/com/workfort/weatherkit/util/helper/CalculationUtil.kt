package com.workfort.weatherkit.util.helper

class CalculationUtil {
    fun toCelsius(kelvin: Float): Float {
        return kelvin - 273.0f
    }
}