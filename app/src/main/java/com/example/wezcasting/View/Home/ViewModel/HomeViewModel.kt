package com.example.wezcasting.View.Home.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherForecastResponse
import com.example.wezcasting.Model.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val weatherRepository: WeatherRepository, var lat : Double, var lon : Double, var lang : String, var unit : String) : ViewModel() {

    private val _data = MutableStateFlow<CurrentWeather?>(null)
    val data : StateFlow<CurrentWeather?> = _data

    private val _dataForecast = MutableStateFlow<WeatherForecastResponse?>(null)
    val dataForecast : StateFlow<WeatherForecastResponse?> = _dataForecast

    fun getCurrentWeather(){
        viewModelScope.launch {
            println("Current unit = " + unit)
            weatherRepository.fetchCurrentWeather(lat,lon,lang,unit).collect{currentWeather ->
                _data.value = currentWeather
            }
        }
    }

    fun getWeatherForecast(){
        viewModelScope.launch {
            println("Forecast unit = " + unit)
            weatherRepository.fetchWeatherForecast(lat,lon, lang, unit,"").collect{weatherForecast ->
                _dataForecast.value = weatherForecast
            }
        }
    }
}