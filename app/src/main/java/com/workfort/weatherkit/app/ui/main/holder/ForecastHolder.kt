package com.workfort.weatherkit.app.ui.main.holder

import android.text.format.DateFormat
import androidx.recyclerview.widget.RecyclerView
import com.workfort.weatherkit.app.data.remote.Forecast
import com.workfort.weatherkit.databinding.ItemForecastBinding
import com.workfort.weatherkit.util.helper.CalculationUtil
import com.workfort.weatherkit.util.helper.WeatherKitUtil

class ForecastHolder(private val binding: ItemForecastBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(forecast: Forecast) {
        binding.tvDay.text = DateFormat.format("HH:mm", forecast.date)
        val temp = "${CalculationUtil().toCelsius(forecast.main.temp).toInt()}\" ${forecast.wind.speed}\'"
        binding.tvTemp.text = temp

        val icon = WeatherKitUtil().getWeatherIcon(forecast.weather[0].main.toLowerCase())
        binding.icWeather.setImageResource(icon)
    }
}