package com.example.wezcasting.Model.interfaces

import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherForecastResponse
import com.example.wezcasting.Networking.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImp(private val weatherService: WeatherService) : IRemoteDataSource {
    override suspend fun fetchCurrentWeather(lat: Double, lon: Double, lang: String, units: String): Flow<CurrentWeather?> = flow {
        val response = weatherService.getCurrentWeather(lat, lon, "YOUR_API_KEY", units, lang)
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            emit(null)
        }
    }

    override suspend fun fetchWeatherForecast(lat: Double, lon: Double, lang: String, units: String, count: String): Flow<WeatherForecastResponse?> = flow {
        val response = weatherService.getWeatherForecast(lat, lon, "YOUR_API_KEY", units, count, lang)
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            emit(null)
        }
    }
}