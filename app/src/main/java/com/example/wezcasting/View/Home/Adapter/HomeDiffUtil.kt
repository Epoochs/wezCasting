package com.example.wezcasting.View.Home.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.Model.WeatherForecastResponse

class HomeDiffUtil : DiffUtil.ItemCallback<WeatherCasting>() {
    override fun areContentsTheSame(oldItem: WeatherCasting, newItem: WeatherCasting): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areItemsTheSame(oldItem: WeatherCasting, newItem: WeatherCasting): Boolean {
        return oldItem == newItem
    }
}