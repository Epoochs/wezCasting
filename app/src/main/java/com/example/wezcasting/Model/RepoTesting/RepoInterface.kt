package com.example.wezcasting.Model.RepoTesting

import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.flow.Flow

interface RepoInterface {

    suspend fun getWeather(long:Double , lat:Double,appid:String,units:String,lang:String): Flow<CurrentWeather>
    suspend fun getAllFavWeather():Flow<List<CurrentWeather>>
    suspend fun insertWeatherToFav(weather:CurrentWeather)
    suspend fun deleteWeatherFromFav(weather:CurrentWeather)

}