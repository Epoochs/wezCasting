package com.example.wezcasting.Model.FakeDataForTest

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.WeatherForecastResponse
import com.example.wezcasting.Model.interfaces.ILocalDataSource

class FakeLocalDataSource : ILocalDataSource {

    private val currentWeatherList = mutableListOf<CurrentWeather>()
    private val alarmList = mutableListOf<Alarm>()

    // Simulating upsert (add or update)
    override suspend fun upsert(weather: CurrentWeather) {
        val index = currentWeatherList.indexOfFirst { it.id == weather.id }
        if (index != -1) {
            currentWeatherList[index] = weather // Update existing entry
        } else {
            currentWeatherList.add(weather) // Add new entry
        }
    }

    // Simulating remove by object
    override suspend fun remove(weather: CurrentWeather) {
        currentWeatherList.remove(weather)
    }

    // Simulating remove alarm
    override suspend fun removeAlarm(alarm: Alarm) {
        alarmList.remove(alarm)
    }

    // Simulating remove by id
    override suspend fun removeById(id: Int) {
        currentWeatherList.removeAll { it.id == id }
    }

    // Simulating remove all
    override suspend fun removeAll() {
        currentWeatherList.clear()
    }

    // Simulating getting all current weather
    override fun getAllCurrentWeather(): Flow<List<CurrentWeather>> {
        return flow { emit(currentWeatherList) }
    }
}
