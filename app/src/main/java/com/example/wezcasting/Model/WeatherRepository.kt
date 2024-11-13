package com.example.wezcasting.Model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.wezcasting.Model.interfaces.ILocalDataSource
import com.example.wezcasting.Model.interfaces.IRemoteDataSource
import com.example.wezcasting.Networking.WeatherService
import com.example.wezcasting.db.WeatherDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch



import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherRepository(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource
) : IWeatherRepository {

    private val _savedData = MutableStateFlow<List<CurrentWeather>?>(emptyList())
    override val savedData: StateFlow<List<CurrentWeather>?> = _savedData

    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null
        fun getInstance(localDataSource: ILocalDataSource, remoteDataSource: IRemoteDataSource): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherRepository(localDataSource, remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun fetchCurrentWeather(lat: Double, lon: Double, lang: String, units: String): Flow<CurrentWeather?> {
        return remoteDataSource.fetchCurrentWeather(lat, lon, lang, units)
    }

    override suspend fun fetchWeatherForecast(lat: Double, lon: Double, lang: String, units: String, count: String): Flow<WeatherForecastResponse?> {
        return remoteDataSource.fetchWeatherForecast(lat, lon, lang, units, count)
    }

    override suspend fun upsert(weather: CurrentWeather) {
        localDataSource.upsert(weather)
    }

    override suspend fun remove(weather: CurrentWeather) {
        localDataSource.remove(weather)
    }

    override suspend fun removeAlarm(alarm: Alarm) {
        localDataSource.removeAlarm(alarm)
    }

    override suspend fun removeById(id: Int) {
        localDataSource.removeById(id)
    }

    override suspend fun removeAll() {
        localDataSource.removeAll()
    }

    override suspend fun getWeatherLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.getAllCurrentWeather().collect { savedCurrentWeather ->
                _savedData.value = savedCurrentWeather
            }
        }
    }
}






/*class WeatherRepository(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource
) {

    private val _savedData = MutableStateFlow<List<CurrentWeather>?>(emptyList())
    val savedData: StateFlow<List<CurrentWeather>?> = _savedData

    companion object {
        private var INSTANCE: WeatherRepository? = null
        fun getInstance(localDataSource: ILocalDataSource, remoteDataSource: IRemoteDataSource): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherRepository(localDataSource, remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun fetchCurrentWeather(lat: Double, lon: Double, lang: String, units: String): Flow<CurrentWeather?> {
        return remoteDataSource.fetchCurrentWeather(lat, lon, lang, units)
    }

    suspend fun fetchWeatherForecast(lat: Double, lon: Double, lang: String, units: String, count: String): Flow<WeatherForecastResponse?> {
        return remoteDataSource.fetchWeatherForecast(lat, lon, lang, units, count)
    }

    suspend fun upsert(weather: CurrentWeather) {
        localDataSource.upsert(weather)
    }

    suspend fun remove(weather: CurrentWeather) {
        localDataSource.remove(weather)
    }

    suspend fun removeAlarm(alarm: Alarm) {
        localDataSource.removeAlarm(alarm)
    }

    suspend fun removeById(id: Int) {
        localDataSource.removeById(id)
    }

    suspend fun removeAll() {
        localDataSource.removeAll()
    }

    suspend fun getWeatherLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.getAllCurrentWeather().collect { savedCurrentWeather ->
                _savedData.value = savedCurrentWeather
            }
        }
    }
}*/





/* WeatherRepository(private val weatherService: WeatherService, private val weatherDatabase: WeatherDatabase){//, private val weatherDatabase: WeatherDatabase){

    private val _savedData = MutableStateFlow<List<CurrentWeather>?>(emptyList())
    val savedData : StateFlow<List<CurrentWeather>?> = _savedData


    companion object{
        private var INSTANCE: WeatherRepository? = null
        fun getInstance(weatherService: WeatherService,weatherDatabase: WeatherDatabase): WeatherRepository{
            return INSTANCE ?: synchronized(this){
                val instance = WeatherRepository(weatherService,weatherDatabase)
                INSTANCE = instance
                instance
            }
        }
    }

    /* Our function to fetch our data */
    suspend fun fetchCurrentWeather(lat : Double, lon : Double, lang : String, units : String) : Flow<CurrentWeather?> = flow {
        var weatherResponse : CurrentWeather? = null
        val response = weatherService.getCurrentWeather(
            lat, // Latitude
            lon, // Longitude
            "79e521039cf107c8d593f9df527860e3", // My API keyID
            units, // Your chosen unit (Standard *Make it empty string*, Metric and Imperial)
            lang // Your Output Language
        )
        if (response.isSuccessful){
            weatherResponse = response.body()
            emit(weatherResponse)
        }else{
            println("Failed to get")
            emit(null)
        }
    }

    suspend fun fetchWeatherForecast(lat : Double, lon : Double, lang : String, units : String, count : String) : Flow<WeatherForecastResponse?> = flow{
        var weatherForecastResponse : WeatherForecastResponse?
        val response = weatherService.getWeatherForecast(
            lat = lat,
            lon = lon,
            apiKey = "79e521039cf107c8d593f9df527860e3",
            units = units,
            cnt = count,
            lang = lang
        )

        if (response.isSuccessful){
            weatherForecastResponse = response.body()
            emit(weatherForecastResponse)
        }else{
            emit(null)
        }
    }

    suspend fun upsert(weather: CurrentWeather?){
        if (weather != null) {
            weatherDatabase.getWeatherDao().upsertWeatherLocation(weather)
        }
    }

    suspend fun remove(weather: CurrentWeather?){
        if (weather != null) {
            weatherDatabase.getWeatherDao().removeWeatherLocation(weather)
        }
    }

    suspend fun removeAlarm(alarm: Alarm){
        if (alarm != null){
            weatherDatabase.alarmDao().deleteAlarm(alarm)
        }
    }

    suspend fun removeById(id:Int){
        weatherDatabase.getWeatherDao().removeWeatherLocationById(id)
    }

    suspend fun removeAll(){
        weatherDatabase.getWeatherDao().removeAllWeatherLocation()
    }

    suspend fun getWeatherLocation(){
        CoroutineScope(Dispatchers.IO).launch {
            weatherDatabase.getWeatherDao().allCurrentWeather().collect{savedCurrentWeather ->
                _savedData.value = savedCurrentWeather
            }
        }
    }
}*/