package com.example.wezcasting.Networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherClinet {
    private val Base_URL = "https://api.openweathermap.org/data/2.5/"
    private val instance =
        Retrofit.Builder().baseUrl(Base_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    val weatherService: WeatherService by lazy {
        instance.create(WeatherService::class.java)
    }
}