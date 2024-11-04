package com.example.wezcasting.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wezcasting.Model.Clouds
import com.example.wezcasting.Model.Coord
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.Main
import com.example.wezcasting.Model.Sys
import com.example.wezcasting.Model.WeatherCurrent
import com.example.wezcasting.Model.Wind
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var weatherDao: WeatherDao
    private lateinit var db: WeatherDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, WeatherDatabase::class.java).build()
        weatherDao = db.getWeatherDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetAllFavWeather() = runBlocking {
        val weather = CurrentWeather(1, Coord(2.3,43.4), listOf(WeatherCurrent(0,"","","")), Main(34.4,54.4,54.5,54.5,3,5,3,5),34,
            Wind(3.3,4,32.3),
            Clouds(4),
            Sys("Egypt",23,23), "Ahmed")

        weatherDao.upsertWeatherLocation(weather)

        val allWeather = weatherDao.allCurrentWeather().first()

        assertEquals(allWeather.size, 1)
        assertEquals(allWeather[0], weather)
    }

    @Test
    fun deleteWeather() = runBlocking {
        val weather = CurrentWeather(1, Coord(2.3,43.4), listOf(WeatherCurrent(0,"","","")), Main(34.4,54.4,54.5,54.5,3,5,3,5),34,
        Wind(3.3,4,32.3),
        Clouds(4),
        Sys("Egypt",23,23), "Ahmed")

        weatherDao.upsertWeatherLocation(weather)
        weatherDao.removeWeatherLocation(weather)

        val allWeather = weatherDao.allCurrentWeather().first()

        assertTrue(allWeather.isEmpty())
    }
}