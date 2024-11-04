package com.example.wezcasting.db

import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherLocalDataSourceImp : IWeatherLocalDataSource {

    private val weatherList = mutableListOf<CurrentWeather>()
    private val alarmList = mutableListOf<Alarm>()

    override suspend fun insertWeather(weather: CurrentWeather) {
        weatherList.add(weather)
    }

    override suspend fun deleteWeather(weather: CurrentWeather) {
        weatherList.remove(weather)
    }

    override suspend fun getAllFavWeather(): Flow<List<CurrentWeather>> {
        return flow { emit(weatherList) }
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        alarmList.add(alarm)
    }

    override fun getAllAlarms(): Flow<List<Alarm>> {
        return flow { emit(alarmList) }
    }
}