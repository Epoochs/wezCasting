package com.example.wezcasting.View.Alert.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.Model.WeatherForecastResponse

class AlertDiffUtil : DiffUtil.ItemCallback<Alarm>() {
    override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem == newItem
    }
}