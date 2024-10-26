package com.example.wezcasting.Model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.wezcasting.Networking.WeatherService
import com.example.wezcasting.db.WeatherDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository(private val weatherService: WeatherService, private val weatherDatabase: WeatherDatabase){//, private val weatherDatabase: WeatherDatabase){

    /* Our function to fetch our data */
    suspend fun fetchCurrentWeather(lat : Double, lon : Double, lang : String, units : String) : Flow<CurrentWeather?> = flow {
        var weatherResponse : CurrentWeather? = null
        val response = weatherService.getCurrentWeather(
            lat, // Latitude
            lon, // Longitude
            "79e521039cf107c8d593f9df527860e3", // My API keyID
            units, // Your chosen unit (Standard *Make it empty string*, Metric and Imperial)
            lang // Your Output Language
        )
        if (response.isSuccessful){
            weatherResponse = response.body()
            emit(weatherResponse)
        }else{
            println("Failed to get")
            emit(null)
        }
    }

    suspend fun fetchWeatherForecast(lat : Double, lon : Double, lang : String, units : String, count : String) : Flow<WeatherForecastResponse?> = flow{
        var weatherForecastResponse : WeatherForecastResponse?
        val response = weatherService.getWeatherForecast(
            lat = lat,
            lon = lon,
            apiKey = "79e521039cf107c8d593f9df527860e3",
            units = units,
            cnt = count,
            lang = lang
        )

        if (response.isSuccessful){
            weatherForecastResponse = response.body()
            emit(weatherForecastResponse)
        }else{
            emit(null)
        }
    }

    suspend fun upsert(weather: CurrentWeather?){
        if (weather != null) {
            weatherDatabase.getWeatherDao().upsertWeatherLocation(weather)
        }
    }

    suspend fun remove(weather: CurrentWeather?){
        if (weather != null) {
            weatherDatabase.getWeatherDao().removeWeatherLocation(weather)
        }
    }

    suspend fun removeAll(){
        weatherDatabase.getWeatherDao().removeAllWeatherLocation()
    }

    suspend fun getWeatherLocation() : LiveData<CurrentWeather>{
        return weatherDatabase.getWeatherDao().allCurrentWeather()
    }
}