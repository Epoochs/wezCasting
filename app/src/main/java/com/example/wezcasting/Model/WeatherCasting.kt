package com.example.wezcasting.Model

import com.google.gson.annotations.SerializedName

data class WeatherCasting (
    val dt : Long,
    val main : MainCast,
    val weather : List<WeatherCast>,
    val clouds: CloudsCast,
    val wind : WindCast,
    val rain : RainCast,
    val sys : SysCast,
    val dt_txt : String?
)

data class MainCast(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("humidity")
    val humidity: Int
)


data class WeatherCast(
    val id : Int,
    val main : String?,
    val description: String?,
    val icon : String?
)

data class CloudsCast(
    val all : Int
)

data class WindCast(
    val speed : Double,
    val deg : Int,
    val gust : Double
)

data class RainCast(
    @SerializedName("3h")
    val threeHours : Double
)

data class SysCast(
    val pod : String?
)