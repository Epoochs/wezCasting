package com.example.wezcasting.View.Favourite.Adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.Model.WeatherForecastResponse

class SavedDiffUtil : DiffUtil.ItemCallback<CurrentWeather>() {
    override fun areContentsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem == newItem
    }
}