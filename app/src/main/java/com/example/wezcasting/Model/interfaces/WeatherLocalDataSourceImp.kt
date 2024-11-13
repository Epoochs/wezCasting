package com.example.wezcasting.Model.interfaces

import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImp(private val weatherDatabase: WeatherDatabase) : ILocalDataSource {
    override suspend fun upsert(weather: CurrentWeather) {
        weatherDatabase.getWeatherDao().upsertWeatherLocation(weather)
    }

    override suspend fun remove(weather: CurrentWeather) {
        weatherDatabase.getWeatherDao().removeWeatherLocation(weather)
    }

    override suspend fun removeAlarm(alarm: Alarm) {
        weatherDatabase.alarmDao().deleteAlarm(alarm)
    }

    override suspend fun removeById(id: Int) {
        weatherDatabase.getWeatherDao().removeWeatherLocationById(id)
    }

    override suspend fun removeAll() {
        weatherDatabase.getWeatherDao().removeAllWeatherLocation()
    }

    override fun getAllCurrentWeather(): Flow<List<CurrentWeather>> {
        return weatherDatabase.getWeatherDao().allCurrentWeather()
    }
}