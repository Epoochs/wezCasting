package com.example.wezcasting.View.Alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.db.AlarmDao

class AlertViewModelFactory(private val alertDao: AlarmDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertViewModel(alertDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}