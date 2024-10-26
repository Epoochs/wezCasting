package com.example.wezcasting.Networking

import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String?,
        @Query("units") units: String?,
        @Query("lang") lang: String?
    ): Response<CurrentWeather>

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String?,
        @Query("units") units: String?,
        @Query("lang") lang: String?,
        @Query("cnt") cnt : String?
    ): Response<WeatherForecastResponse>
}