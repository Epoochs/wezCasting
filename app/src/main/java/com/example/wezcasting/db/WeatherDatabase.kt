package com.example.wezcasting.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wezcasting.Model.Converters
import com.example.wezcasting.Model.CurrentWeather

@Database(entities = arrayOf(CurrentWeather::class), version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase(){
    abstract fun getWeatherDao() : WeatherDao

    companion object {
        @Volatile
        private var weatherDatabase: WeatherDatabase? = null

        /* This operator function in Companion object is going to call getInstance() whenever ProductDatabase is invoked */
        operator fun invoke(context: Context) = weatherDatabase ?: synchronized(Any()){
            weatherDatabase ?: getInstance(context).also { weatherDatabase = it  }
        }

        fun getInstance(context: Context): WeatherDatabase {
            return weatherDatabase ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java, "Weather Table").build()
                weatherDatabase = instance
                instance
            }
        }
    }
}