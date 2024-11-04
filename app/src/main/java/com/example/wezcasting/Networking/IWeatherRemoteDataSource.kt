package com.example.wezcasting.Networking

import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherForecastResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherRemoteDataSource {

    suspend fun fetchCurrentWeather(lon:Double,lat:Double,apid:String,units:String,lang:String): Flow<CurrentWeather>
}