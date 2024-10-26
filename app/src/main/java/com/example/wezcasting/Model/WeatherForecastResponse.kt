package com.example.wezcasting.Model

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(val cod : String ,val list : List<WeatherCasting>, val city: City)

data class City(
    @SerializedName("name")
    val name: String,
    @SerializedName("coord")
    val coord: Coord,
    @SerializedName("country")
    val country: String,
    @SerializedName("population")
    val population: Int,
    @SerializedName("timezone")
    val timezone: Int,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long
)