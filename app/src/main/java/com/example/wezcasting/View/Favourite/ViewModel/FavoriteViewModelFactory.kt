package com.example.wezcasting.View.Favourite.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wezcasting.Model.WeatherRepository

class FavoriteViewModelFactory(private val weatherRepository: WeatherRepository, var lat : Double, var lon : Double) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            (println ("FavFacory" + lat))
            return FavoriteViewModel(weatherRepository,lat,lon) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}