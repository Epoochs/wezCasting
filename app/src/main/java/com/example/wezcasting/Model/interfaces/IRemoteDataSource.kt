package com.example.wezcasting.Model.interfaces

import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherForecastResponse
import kotlinx.coroutines.flow.Flow

interface IRemoteDataSource {
    suspend fun fetchCurrentWeather(lat: Double, lon: Double, lang: String, units: String): Flow<CurrentWeather?>
    suspend fun fetchWeatherForecast(lat: Double, lon: Double, lang: String, units: String, count: String): Flow<WeatherForecastResponse?>
}