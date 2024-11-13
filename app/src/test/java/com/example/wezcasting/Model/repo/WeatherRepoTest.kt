package com.example.wezcasting.Model.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wezcasting.Model.Clouds
import com.example.wezcasting.Model.Coord
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.FakeDataForTest.FakeLocalDataSource
import com.example.wezcasting.Model.FakeDataForTest.FakeRemoteDataSource
import com.example.wezcasting.Model.Main
import com.example.wezcasting.Model.Sys
import com.example.wezcasting.Model.WeatherCurrent
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.Model.Wind
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.flow.first
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsEqual

@RunWith(AndroidJUnit4::class)
class WeatherRepositoryTest {

    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = WeatherRepository(fakeLocalDataSource, fakeRemoteDataSource)
    }

    @Test
    fun upsertWeatherForecast_retrievesSameForecast() = runTest {
        // Given
        val mockWeather = CurrentWeather(
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

        // When
        repository.upsert(mockWeather) // Insert the mock data
        val retrievedWeather = fakeLocalDataSource.getAllCurrentWeather().first() // Collect the first emitted value

        // Then
        assertThat(retrievedWeather, IsEqual(listOf(mockWeather))) // Compare lists
    }
}



