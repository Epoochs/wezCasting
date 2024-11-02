package com.example.wezcasting.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wezcasting.Model.CurrentWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_table")
    fun allCurrentWeather() : Flow<List<CurrentWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeatherLocation(weather: CurrentWeather)

    @Delete
    suspend fun removeWeatherLocation(weather: CurrentWeather)

    @Query("DELETE FROM weather_table WHERE id = :id")
    suspend fun removeWeatherLocationById(id : Int)

    @Query("DELETE FROM weather_table")
    suspend fun removeAllWeatherLocation()
}