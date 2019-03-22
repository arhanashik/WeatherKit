package com.workfort.weatherkit.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.weatherkit.R
import com.workfort.weatherkit.app.data.remote.Forecast
import com.workfort.weatherkit.app.ui.main.holder.ForecastHolder
import com.workfort.weatherkit.databinding.ItemForecastBinding

class ForecastAdapter: RecyclerView.Adapter<ForecastHolder>() {
    private val forecastList: ArrayList<Forecast> = ArrayList()

    fun setForecastList(forecastList: ArrayList<Forecast>) {
        this.forecastList.clear()
        this.forecastList.addAll(forecastList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        val binding = DataBindingUtil.inflate<ItemForecastBinding>(
            LayoutInflater.from(parent.context), R.layout.item_forecast, parent, false
        )

        return ForecastHolder(binding)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        holder.bind(forecastList[position])
    }
}