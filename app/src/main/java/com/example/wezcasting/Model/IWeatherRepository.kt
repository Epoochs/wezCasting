package com.example.wezcasting.Model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IWeatherRepository {

    // Fetch current weather from remote source
    suspend fun fetchCurrentWeather(lat: Double, lon: Double, lang: String, units: String): Flow<CurrentWeather?>

    // Fetch weather forecast from remote source
    suspend fun fetchWeatherForecast(lat: Double, lon: Double, lang: String, units: String, count: String): Flow<WeatherForecastResponse?>

    // Local Data operations
    suspend fun upsert(weather: CurrentWeather)

    suspend fun remove(weather: CurrentWeather)

    suspend fun removeAlarm(alarm: Alarm)

    suspend fun removeById(id: Int)

    suspend fun removeAll()

    suspend fun getWeatherLocation()

    // StateFlow that emits the saved data for UI observation
    val savedData: StateFlow<List<CurrentWeather>?>
}

