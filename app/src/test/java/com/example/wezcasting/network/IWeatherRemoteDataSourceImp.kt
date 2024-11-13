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
            CurrentWeather(
                id = 1,  // The ID of the weather entry (can be any integer)
                tempUnit = "c",  // Temperature unit (Celsius)
                coord = Coord(
                    lon = 2.3,  // Longitude value
                    lat = 43.4   // Latitude value
                ),
                weather = listOf(
                    WeatherCurrent(
                        id = 0,  // Weather condition ID (for example, 0 could be "Clear Sky")
                        main = "",  // Weather type (e.g., "Clear", "Clouds", etc.)
                        description = "",  // Description of the weather (e.g., "Clear sky")
                        icon = ""  // Icon representing the weather condition (e.g., "01d" for clear sky)
                    )
                ),
                main = Main(
                    temp = 34.4,  // Temperature in Celsius
                    feels_like = 54.4,  // Feels like temperature in Celsius
                    temp_min = 54.5,  // Minimum temperature in Celsius
                    temp_max = 54.5,  // Maximum temperature in Celsius
                    pressure = 3,  // Atmospheric pressure in hPa
                    humidity = 5,  // Humidity percentage
                    sea_level = 3,  // Sea level pressure in hPa
                    grnd_level = 5  // Ground level pressure in hPa
                ),
                visibility = 34,  // Visibility in meters
                wind = Wind(
                    speed = 3.3,  // Wind speed in m/s
                    deg = 4,  // Wind direction in degrees (e.g., 0 = North)
                    gust = 32.3  // Wind gust speed in m/s
                ),
                clouds = Clouds(
                    all = 4  // Cloudiness percentage (e.g., 0 for clear sky, 100 for full cloud cover)
                ),
                sys = Sys(
                    country = "Egypt",  // Country name
                    sunrise = 23,  // Sunrise time (timestamp in seconds)
                    sunset = 23  // Sunset time (timestamp in seconds)
                ),
                name = "Ahmed"  // City name
            )
        )
    }
}
