package com.example.wezcasting.Model

interface OnLocationUpdates {
    fun getCurrentWeather(lat:Double, lon : Double)
    fun getWeatherForecast(lat:Double, lon : Double)
}