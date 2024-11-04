package com.example.wezcasting.Model.repo

import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.db.FakeWeatherLocalDataSourceImp
import com.example.wezcasting.network.IFakeWeatherRemoteDataSourceImp
import junit.framework.TestCase.assertNotNull
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.runBlocking

class RepoTest {
    lateinit var remoteDataSource: IFakeWeatherRemoteDataSourceImp
    lateinit var localDataSource: FakeWeatherLocalDataSourceImp
    lateinit var repo: WeatherRepository

    @Before
    fun setUp() {
        remoteDataSource = IFakeWeatherRemoteDataSourceImp()
        localDataSource = FakeWeatherLocalDataSourceImp()
    }

    @Test
    fun getWeather_withMetricUnit_returnsCelsiusTemperature() = runBlocking {

        val lat = 12.0
        val lon = 12.0
        val units = "metric"
        val lang = "en"
        val api="0f121088b1919d00bf3ffec84d4357f9"


        val weatherResponse = repo.fetchCurrentWeather(lat,lon,lang,units)

        assertNotNull(weatherResponse)
    }
}