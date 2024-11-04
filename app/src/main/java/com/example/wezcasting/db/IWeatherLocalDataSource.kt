package com.example.wezcasting.db

import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun insertWeather(weather:CurrentWeather)
    suspend fun deleteWeather(weather:CurrentWeather)
    suspend fun getAllFavWeather(): Flow<List<CurrentWeather>>

    suspend fun insertAlarm(alarm: Alarm)
    fun getAllAlarms(): Flow<List<Alarm>>
}