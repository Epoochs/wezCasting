package com.example.wezcasting.network

import com.example.wezcasting.Model.Clouds
import com.example.wezcasting.Model.Coord
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.Main
import com.example.wezcasting.Model.Sys
import com.example.wezcasting.Model.WeatherCurrent
import com.example.wezcasting.Model.WeatherForecastResponse
import com.example.wezcasting.Model.Wind
import com.example.wezcasting.Networking.IWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class IFakeWeatherRemoteDataSourceImp : IWeatherRemoteDataSource {

    override suspend fun fetchCurrentWeather(lon: Double, lat: Double, apid: String, units: String, lang: String): Flow<CurrentWeather> {
        return flowOf(
            CurrentWeather(1, Coord(2.3,43.4), listOf(WeatherCurrent(0,"","","")), Main(34.4,54.4,54.5,54.5,3,5,3,5),34,
                Wind(3.3,4,32.3),
                Clouds(4),
                Sys("Egypt",23,23), "Ahmed")
        )
    }
}
