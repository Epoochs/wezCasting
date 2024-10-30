package com.example.wezcasting.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "weather_table")
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val coord: Coord,
    val weather: List<WeatherCurrent>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val sys: Sys,
    val name: String
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class WeatherCurrent(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like : Double,
    val temp_min : Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level : Int,
    val grnd_level : Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust : Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

class Converters {

    @TypeConverter
    fun fromWeatherList(weather: List<WeatherCurrent>?): String {
        return Gson().toJson(weather)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<WeatherCurrent> {
        return try {
            Gson().fromJson<ArrayList<WeatherCurrent>>(value)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    @TypeConverter
    fun toCoord(value: String?): Coord {
        return Gson().fromJson(value, Coord::class.java)
    }

    @TypeConverter
    fun fromCoord(coord: Coord?): String {
        return Gson().toJson(coord)
    }

    @TypeConverter
    fun toMain(value: String?): Main {
        return Gson().fromJson(value, Main::class.java)
    }

    @TypeConverter
    fun fromMain(main: Main?): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toWind(value: String?): Wind {
        return Gson().fromJson(value, Wind::class.java)
    }

    @TypeConverter
    fun fromWind(wind: Wind?): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toCloud(value: String?): Clouds {
        return Gson().fromJson(value, Clouds::class.java)
    }

    @TypeConverter
    fun fromCloud(clouds: Clouds?): String {
        return Gson().toJson(clouds)
    }

    @TypeConverter
    fun toSys(value: String?): Sys {
        return Gson().fromJson(value, Sys::class.java)
    }

    @TypeConverter
    fun fromSys(clouds: Sys?): String {
        return Gson().toJson(clouds)
    }
}
