package com.example.wezcasting.View.Home.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wezcasting.Model.WeatherRepository

class HomeViewModelFactory(private val weatherRepository: WeatherRepository, var lat : Double, var lon : Double) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(weatherRepository,lat,lon) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}