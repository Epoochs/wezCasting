package com.example.wezcasting.View.Favourite.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherCurrent
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val weatherRepository: WeatherRepository, var lat : Double, var lon : Double) : ViewModel() {

  //  val savedWeatherData: StateFlow<CurrentWeather?> = weatherRepository.savedData
    private val _data = MutableStateFlow<CurrentWeather?>(null)
    val data : StateFlow<CurrentWeather?> = _data

    init {
        viewModelScope.launch {
            weatherRepository.getWeatherLocation()
        }
    }

    fun upsert(current: CurrentWeather){
        viewModelScope.launch {
            weatherRepository.upsert(current)
        }
    }

    fun remove(current: CurrentWeather){
        viewModelScope.launch {
            weatherRepository.remove(current)
        }
    }

    fun removeAll(){
        viewModelScope.launch {
            weatherRepository.removeAll()
        }
    }

    fun getCurrentWeather(lang : String, unit : String){
        println("getCurrent" + lat)
        viewModelScope.launch {
            weatherRepository.fetchCurrentWeather(lat,lon,lang,unit).collect{currentWeather ->
                _data.value = currentWeather
            }
        }
    }

    fun updateCoordinates(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
        _data.value = null
    }
}