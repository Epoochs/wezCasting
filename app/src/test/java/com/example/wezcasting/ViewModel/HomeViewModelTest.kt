package com.example.wezcasting.View.Home.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wezcasting.Model.City
import com.example.wezcasting.Model.Clouds
import com.example.wezcasting.Model.CloudsCast
import com.example.wezcasting.Model.Coord
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.Main
import com.example.wezcasting.Model.MainCast
import com.example.wezcasting.Model.RainCast
import com.example.wezcasting.Model.Sys
import com.example.wezcasting.Model.SysCast
import com.example.wezcasting.Model.WeatherCast
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.Model.WeatherCurrent
import com.example.wezcasting.Model.WeatherForecastResponse
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.Model.Wind
import com.example.wezcasting.Model.WindCast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()  // For LiveData and StateFlow tests

    @Mock
    lateinit var weatherRepository: WeatherRepository  // Mocked WeatherRepository

    private lateinit var homeViewModel: HomeViewModel

    // Test CoroutineDispatcher to avoid blocking main thread
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        // Initialize mock objects
        MockitoAnnotations.openMocks(this)

        // Initialize the HomeViewModel with mocked dependencies
        homeViewModel = HomeViewModel(weatherRepository, 10.0, 20.0, "en", "metric")
    }

    // Test Case 1: Test getCurrentWeather() method
    @Test
    fun testGetCurrentWeather() = testScope.runBlockingTest {
        // Prepare mocked data for the current weather
        val mockedWeather = CurrentWeather(
            id = 1,
            tempUnit = "metric",
            coord = Coord(lon = 10.0, lat = 20.0),
            weather = listOf(
                WeatherCurrent(id = 801, main = "Clouds", description = "Scattered clouds", icon = "03d")
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
            visibility = 10000,
            wind = Wind(speed = 5.0, deg = 180, gust = 6.0),
            clouds = Clouds(all = 50),
            sys = Sys(country = "US", sunrise = 1609459200, sunset = 1609502400),
            name = "Sample City"
        )

        // Stub the repository to return a flow of mocked data
        Mockito.`when`(weatherRepository.fetchCurrentWeather(10.0, 20.0, "en", "metric"))
            .thenReturn(flowOf(mockedWeather))

        // Call the method you want to test
        homeViewModel.getCurrentWeather()

        // Collect from the StateFlow inside the test coroutine scope
        homeViewModel.data.collect { result ->
            assertNotNull(result)  // Verify that the result is not null
            assertEquals(mockedWeather, result)  // Verify that the returned object is the mocked one
        }
    }

    // Test Case 2: Test getWeatherForecast() method
    @Test
    fun testGetWeatherForecast() = testScope.runBlockingTest {
        // Prepare mocked forecast data for the weather forecast
        val mockedForecast = WeatherForecastResponse(
            cod = "200",
            list = listOf(
                WeatherCasting(
                    dt = 1609455600,
                    main = MainCast(temp = 25.0, feelsLike = 24.5, tempMin = 22.0, tempMax = 28.0, pressure = 1013, humidity = 60),
                    weather = listOf(WeatherCast(id = 801, main = "Clouds", description = "Scattered clouds", icon = "03d")),
                    clouds = CloudsCast(all = 50),
                    wind = WindCast(speed = 5.0, deg = 180, gust = 6.0),
                    rain = RainCast(threeHours = 0.0),
                    sys = SysCast(pod = "d"),
                    dt_txt = "2021-01-01 00:00:00"
                ),
                WeatherCasting(
                    dt = 1609466400,
                    main = MainCast(temp = 2.0, feelsLike = 4.5, tempMin = 2.0, tempMax = 2.0, pressure = 1014, humidity = 65),
                    weather = listOf(WeatherCast(id = 871, main = "Clouds", description = "Scattered clouds", icon = "03d")),
                    clouds = CloudsCast(all = 50),
                    wind = WindCast(speed = 5.0, deg = 180, gust = 6.0),
                    rain = RainCast(threeHours = 0.0),
                    sys = SysCast(pod = "d"),
                    dt_txt = "2021-01-01 03:00:00"
                )
            ),
            city = City(
                name = "Sample City",  // City name
                coord = Coord(lon = 10.0, lat = 20.0),  // Coordinates of the city
                country = "US",  // Country code
                population = 1000000,  // Population of the city
                timezone = -18000,  // Timezone in seconds (e.g., UTC-5)
                sunrise = 1609455600,  // Sunrise timestamp
                sunset = 1609502400  // Sunset timestamp
            )
        )

        // Stub the repository to return a flow of mocked forecast data
        Mockito.`when`(weatherRepository.fetchWeatherForecast(10.0, 20.0, "en", "metric", ""))
            .thenReturn(flowOf(mockedForecast))

        // Call the method you want to test
        homeViewModel.getWeatherForecast()

        // Collect from the StateFlow inside the test coroutine scope
        homeViewModel.dataForecast.collect { result ->
            assertNotNull(result)  // Verify that the result is not null
            assertEquals(mockedForecast, result)  // Verify that the returned object is the mocked one
        }
    }

    // Cleanup
    @Test
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
}
