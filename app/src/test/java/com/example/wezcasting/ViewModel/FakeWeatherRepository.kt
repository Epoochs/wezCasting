package com.example.wezcasting.ViewModel

import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.City
import com.example.wezcasting.Model.Clouds
import com.example.wezcasting.Model.CloudsCast
import com.example.wezcasting.Model.Coord
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.IWeatherRepository
import com.example.wezcasting.Model.Main
import com.example.wezcasting.Model.MainCast
import com.example.wezcasting.Model.RainCast
import com.example.wezcasting.Model.Sys
import com.example.wezcasting.Model.SysCast
import com.example.wezcasting.Model.WeatherCast
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.Model.WeatherCurrent
import com.example.wezcasting.Model.WeatherForecastResponse
import com.example.wezcasting.Model.Wind
import com.example.wezcasting.Model.WindCast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FakeWeatherRepository : IWeatherRepository {

    private val _savedData = MutableStateFlow<List<CurrentWeather>?>(emptyList())
    override val savedData: StateFlow<List<CurrentWeather>?> = _savedData

    // Mock data for testing
    private val mockWeatherData = listOf(
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
        ),
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
            name = "Sherif"  // City name
        )
    )

    private val mockWeatherForecastResponse = WeatherForecastResponse(
        cod = "200",
        list = listOf(
            WeatherCasting(
                dt = 1624017600,
                main = MainCast(temp = 22.5, feelsLike = 21.0, tempMin = 21.0, tempMax = 24.0, pressure = 1012, humidity = 65),
                weather = listOf(WeatherCast(id = 1, main = "Clear", description = "Clear sky", icon = "01d")),
                clouds = CloudsCast(all = 0),
                wind = WindCast(speed = 5.0, deg = 200, gust = 7.0),
                rain = RainCast(threeHours = 0.0),
                sys = SysCast(pod = "d"),
                dt_txt = "2024-11-13 12:00:00"
            ),
            WeatherCasting(
                dt = 1624021200,
                main = MainCast(temp = 20.0, feelsLike = 19.0, tempMin = 18.0, tempMax = 22.0, pressure = 1010, humidity = 70),
                weather = listOf(WeatherCast(id = 2, main = "Clouds", description = "Scattered clouds", icon = "02d")),
                clouds = CloudsCast(all = 30),
                wind = WindCast(speed = 3.5, deg = 190, gust = 5.0),
                rain = RainCast(threeHours = 0.0),
                sys = SysCast(pod = "d"),
                dt_txt = "2024-11-13 15:00:00"
            )
        ),
        city = City(
            name = "TestCity",
            coord = Coord(lon = 12.34, lat = 56.78),
            country = "US",
            population = 100000,
            timezone = -14400,
            sunrise = 1624008000,
            sunset = 1624051200
        )
    )

    override suspend fun fetchCurrentWeather(lat: Double, lon: Double, lang: String, units: String): Flow<CurrentWeather?> {
        return flow {
            // Simulate fetching current weather with a delay (mock network call)
            emit(mockWeatherData.firstOrNull()) // Return a mock weather
        }
    }

    override suspend fun fetchWeatherForecast(lat: Double, lon: Double, lang: String, units: String, count: String): Flow<WeatherForecastResponse?> {
        return flow {
            // Simulate fetching weather forecast (mock data)
            emit(mockWeatherForecastResponse) // Return mock response
        }
    }

    override suspend fun upsert(weather: CurrentWeather) {
        // Mock inserting the weather to local data source
        val updatedList = _savedData.value?.toMutableList()?.apply {
            add(weather)
        }
        _savedData.value = updatedList
    }

    override suspend fun remove(weather: CurrentWeather) {
        // Mock removing a weather from local data source
        val updatedList = _savedData.value?.toMutableList()?.apply {
            remove(weather)
        }
        _savedData.value = updatedList
    }

    override suspend fun removeAlarm(alarm: Alarm) {
        // Do nothing for the mock
    }

    override suspend fun removeById(id: Int) {
        // Mock removing by ID
        val updatedList = _savedData.value?.toMutableList()?.apply {
            removeAll { it.id == id }
        }
        _savedData.value = updatedList
    }

    override suspend fun removeAll() {
        // Mock removing all data
        _savedData.value = emptyList()
    }

    override suspend fun getWeatherLocation() {
        // Mock getting weather location and emit saved data
        _savedData.value = mockWeatherData
    }
}
