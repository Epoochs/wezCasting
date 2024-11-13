package com.example.wezcasting.db

import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.interfaces.ILocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource : ILocalDataSource {

    private val weatherData = mutableListOf<CurrentWeather>()
    private val _weatherFlow = MutableStateFlow<List<CurrentWeather>>(emptyList())
    private val alarmData = mutableListOf<Alarm>()

    override suspend fun upsert(weather: CurrentWeather) {
        // Remove existing entry with the same ID if it exists, and add the new entry
        weatherData.removeIf { it.id == weather.id }
        weatherData.add(weather)
        _weatherFlow.value = weatherData
    }

    override suspend fun remove(weather: CurrentWeather) {
        weatherData.removeIf { it.id == weather.id }
        _weatherFlow.value = weatherData
    }

    override suspend fun removeAlarm(alarm: Alarm) {
        alarmData.removeIf { it.id == alarm.id }
    }

    override suspend fun removeById(id: Int) {
        weatherData.removeIf { it.id == id }
        _weatherFlow.value = weatherData
    }

    override suspend fun removeAll() {
        weatherData.clear()
        _weatherFlow.value = emptyList()
    }

    override fun getAllCurrentWeather(): Flow<List<CurrentWeather>> {
        return _weatherFlow.asStateFlow()
    }
}
