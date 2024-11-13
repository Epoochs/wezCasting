package com.example.wezcasting.Model.interfaces

import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    suspend fun upsert(weather: CurrentWeather)
    suspend fun remove(weather: CurrentWeather)
    suspend fun removeAlarm(alarm: Alarm)
    suspend fun removeById(id: Int)
    suspend fun removeAll()
    fun getAllCurrentWeather(): Flow<List<CurrentWeather>>
}