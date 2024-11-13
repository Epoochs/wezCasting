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
            id = 1,
            tempUnit = "c",
            coord = Coord(
                lon = 2.3,
                lat = 43.4
            ),
            weather = listOf(
                WeatherCurrent(
                    id = 0,
                    main = "",
                    description = "",
                    icon = ""
                )
            ),
            main = Main(
                temp = 34.4,
                feels_like = 54.4,
                temp_min = 54.5,
                temp_max = 54.5,
                pressure = 3,
                humidity = 5,
                sea_level = 3,
                grnd_level = 5
            ),
            visibility = 34,
            wind = Wind(
                speed = 3.3,
                deg = 4,
                gust = 32.3
            ),
            clouds = Clouds(
                all = 4
            ),
            sys = Sys(
                country = "Egypt",
                sunrise = 23,
                sunset = 23
            ),
            name = "Sherif"
        )

        // When
        repository.upsert(mockWeather)
        val retrievedWeather = fakeLocalDataSource.getAllCurrentWeather().first()

        // Then
        assertThat(retrievedWeather, IsEqual(listOf(mockWeather)))
    }
}



