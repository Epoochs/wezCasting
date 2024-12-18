package com.example.wezcasting

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wezcasting.Model.LocationRepository
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.Model.interfaces.WeatherLocalDataSourceImp
import com.example.wezcasting.Model.interfaces.WeatherRemoteDataSourceImp
import com.example.wezcasting.Networking.WeatherClinet
import com.example.wezcasting.Networking.WeatherService
import com.example.wezcasting.View.Home.Adapter.MyDailyDetailsAdapter
import com.example.wezcasting.View.Home.Adapter.MyFiveDaysDetailsAdapter
import com.example.wezcasting.View.Home.ViewModel.HomeViewModel
import com.example.wezcasting.View.Home.ViewModel.HomeViewModelFactory
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    var lat: Double = 0.0
    var lon: Double = 0.0
    var id: Int = 0
    var lang: String = ""
    var unit: String = ""
    var genUnit: String = ""

    lateinit var unSaveButton: ImageButton

    /* Recycle views and Adapters for Hourly details */
    lateinit var recyclerViewHourlyDetails: RecyclerView
    lateinit var myDailyDetailsAdapter: MyDailyDetailsAdapter
    lateinit var hourlyLayoutManager: LinearLayoutManager

    /* Recycle views and Adapters for five days */
    lateinit var recycleViewFiveDays: RecyclerView
    lateinit var myFiveDaysDetailsAdapter: MyFiveDaysDetailsAdapter
    lateinit var fiveLinearLayoutManager: LinearLayoutManager


    /* Weather fetching and data Representation */
    lateinit var weatherService: WeatherService
    lateinit var weatherRepository: WeatherRepository
    lateinit var locationRepository: LocationRepository
    lateinit var homeViewModel: HomeViewModel
    lateinit var weatherDatabase: WeatherDatabase

    /*SharedViewModel between Home and Setting fragments */
    lateinit var sharedVM: HomeSettingsSharedVM

    /* View Components */
    lateinit var tvTownCurrentTemp: TextView
    lateinit var tvWeatherState: TextView
    lateinit var tvSunSetTime: TextView
    lateinit var tvSunRiseTime: TextView
    lateinit var tvHighestTemp: TextView
    lateinit var tvLowestTemp: TextView
    lateinit var tvTownName: TextView
    lateinit var tvVisibility: TextView
    lateinit var tvVisibilityDesc: TextView
    lateinit var tvHumidity: TextView
    lateinit var tvHumidityDesc: TextView
    lateinit var tvFeelsLike: TextView
    lateinit var tvFeelsLikeDesc: TextView
    lateinit var tvPressure: TextView
    lateinit var tvPressureDesc: TextView
    lateinit var tvSpeed: TextView
    lateinit var tvGust: TextView

    /* Units */
    lateinit var tvVisibilityUnit: TextView
    lateinit var tvHumidityUnit: TextView
    lateinit var tvTempUnit: TextView
    lateinit var tvWindSpeedUnit: TextView
    lateinit var tvWindGustUnit: TextView
    lateinit var tvPressureUnit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weather)

        unSaveButton = findViewById(R.id.my_unSave_button)


        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        lat = intent.getDoubleExtra("Lat", 0.0)
        lon = intent.getDoubleExtra("Lon", 0.0)
        lang = intent.getStringExtra("AdapterLang").toString()
        unit = intent.getStringExtra("AdapterUnit").toString()
        genUnit = intent.getStringExtra("General Unit").toString()
        id = intent.getIntExtra("id", 0)

        val weatherService = WeatherClinet.weatherService
        val weatherDatabase = WeatherDatabase.getInstance(this)

        val localDataSource = WeatherLocalDataSourceImp(weatherDatabase)
        val remoteDataSource = WeatherRemoteDataSourceImp(weatherService)
        weatherRepository = WeatherRepository.getInstance(localDataSource, remoteDataSource)

        val factory = HomeViewModelFactory(weatherRepository, lat, lon, lang, genUnit)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)


        init()

        getSavedWeather(lat, lon)
        unSaveButton.setOnClickListener {
            lifecycleScope.launch {
                weatherRepository.removeById(id)
                finish()
            }
        }


    }

    fun init() {
        tvSpeed = findViewById(R.id.tvSpeed)
        tvGust = findViewById(R.id.tvGust)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvPressure = findViewById(R.id.tvPressure)
        tvVisibility = findViewById(R.id.tvVisibility)
        tvFeelsLike = findViewById(R.id.tvFeelsLike)
        tvHighestTemp = findViewById(R.id.tvHighestTemp)
        tvLowestTemp = findViewById(R.id.tvLowestTemp)
        tvWeatherState = findViewById(R.id.tvWeatherState)
        tvSunRiseTime = findViewById(R.id.tvSunRiseTime)
        tvSunSetTime = findViewById(R.id.tvSunSetTime)
        tvTempUnit = findViewById(R.id.tvTempUnit)
        tvTownName = findViewById(R.id.tvTownName)
        tvTownCurrentTemp = findViewById(R.id.tvTownCurrentTemp)

        /* Units */
        tvVisibilityUnit = findViewById(R.id.tvVisibilityUnit)

        tvPressureDesc = findViewById(R.id.tvPressureDesc)
        tvVisibilityDesc = findViewById(R.id.tvVisibilityDesc)
        tvFeelsLikeDesc = findViewById(R.id.tvFeelsLikeDesc)
        tvHumidityDesc = findViewById(R.id.tvHumidityDesc)

        recyclerViewHourlyDetails = findViewById(R.id.rvHourDetails)
        hourlyLayoutManager = LinearLayoutManager(this)
        hourlyLayoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerViewHourlyDetails.setLayoutManager(hourlyLayoutManager)
        myDailyDetailsAdapter = MyDailyDetailsAdapter(this, emptyList())
        recyclerViewHourlyDetails.setAdapter(myDailyDetailsAdapter)


        recycleViewFiveDays = findViewById(R.id.rvWeatherForecasting)
        fiveLinearLayoutManager = LinearLayoutManager(this)
        fiveLinearLayoutManager.orientation = RecyclerView.VERTICAL
        recycleViewFiveDays.layoutManager = fiveLinearLayoutManager
        myFiveDaysDetailsAdapter = MyFiveDaysDetailsAdapter(this, emptyList())
        recycleViewFiveDays.adapter = myFiveDaysDetailsAdapter
        val divider =
            DividerItemDecoration(recycleViewFiveDays.getContext(), DividerItemDecoration.VERTICAL)
        recycleViewFiveDays.addItemDecoration(divider)
    }

    fun getSavedWeather(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon

        println("Current Weather : " + lat)
        println("Current Weather : " + lon)

        // isAdded is used to check whether the fragment is added to the activity or not.
        if (!isDestroyed) {

            homeViewModel.getCurrentWeather()

            lifecycleScope.launch {
                homeViewModel.data.collect { currentWeather ->
                    if (currentWeather != null) {
                        println("INSIDE : " + unit)
                        if (genUnit.equals("metric")) {
                            tvTownCurrentTemp.text = currentWeather.main.temp.toInt().toString()
                            tvTempUnit.text = "°C"

                            /* Highest and Lowest Temp */
                            tvHighestTemp.text =
                                "H:" + currentWeather.main.temp_max.toInt() + "°"
                            tvLowestTemp.text =
                                "L:" + currentWeather.main.temp_min.toInt() + "°"

                            /* Feels-like Temp */
                            tvFeelsLike.text =
                                "" + currentWeather.main.feels_like.toInt().toString() + "°"
                            if (currentWeather.main.feels_like == currentWeather.main.temp) {
                                tvFeelsLikeDesc.text = "it feels exactly the same"
                            } else {
                                if (currentWeather.main.feels_like > currentWeather.main.temp) {
                                    tvFeelsLikeDesc.text = "it feels hotter"
                                } else {
                                    tvFeelsLikeDesc.text = "it feels cooler"
                                }
                            }

                            var windSpeed = currentWeather.wind.speed * 3.6
                            var windGust = currentWeather.wind.gust * 3.6
                            tvGust.text = String.format("%.2f KM/H", windGust)
                            tvSpeed.text = String.format("%.2f KM/H", windSpeed)

                        } else {
                            if (genUnit.equals("imperial")) {
                                println("Imperial ya wa7sh")
                                var currentTemp =
                                    (currentWeather.main.temp).toInt()
                                tvTownCurrentTemp.text = currentTemp.toString()
                                tvTempUnit.text = "°F"

                                var highTemp_f =
                                    (currentWeather.main.temp_max).toInt()
                                var lowTemp_f =
                                    (currentWeather.main.temp_min).toInt()
                                tvHighestTemp.text = "H:" + highTemp_f + "°"
                                tvLowestTemp.text = "L:" + lowTemp_f + "°"

                                /* Feels-like Temp */
                                var feelLikeTemp_f =
                                    (currentWeather.main.feels_like).toInt()
                                tvFeelsLike.text = "" + feelLikeTemp_f.toString() + "°"
                                if (feelLikeTemp_f == currentTemp) {
                                    tvFeelsLikeDesc.text = "it feels exactly the same"
                                } else {
                                    if (feelLikeTemp_f > currentTemp) {
                                        tvFeelsLikeDesc.text = "it feels hotter"
                                    } else {
                                        tvFeelsLikeDesc.text = "it feels cooler"
                                    }
                                }

                                var windSpeed = currentWeather.wind.speed * 2.237
                                var windGust = currentWeather.wind.gust * 2.237
                                tvGust.text = String.format("%.2f MPH", windGust)
                                tvSpeed.text = String.format("%.2f MPH", windSpeed)

                            } else {
                                var currentTemp = (currentWeather.main.temp).toInt()
                                tvTownCurrentTemp.text = currentTemp.toString()
                                tvTempUnit.text = "°K"

                                tvHighestTemp.text =
                                    "H:" + (currentWeather.main.temp_max).toInt() + "°"
                                tvLowestTemp.text =
                                    "L:" + (currentWeather.main.temp_min).toInt() + "°"

                                /* Feels-like Temp */
                                var feelLikeTemp_k =
                                    (currentWeather.main.feels_like).toInt()
                                tvFeelsLike.text = "" + feelLikeTemp_k.toString() + "°"
                                if (feelLikeTemp_k == currentTemp) {
                                    tvFeelsLikeDesc.text = "it feels exactly the same"
                                } else {
                                    if (feelLikeTemp_k > currentTemp) {
                                        tvFeelsLikeDesc.text = "it feels hotter"
                                    } else {
                                        tvFeelsLikeDesc.text = "it feels cooler"
                                    }
                                }
                                var windSpeed = currentWeather.wind.speed * 2.237
                                var windGust = currentWeather.wind.gust * 2.237
                                tvGust.text = String.format("%.2f MPH", windGust)
                                tvSpeed.text = String.format("%.2f MPH", windSpeed)
                            }
                        }

                        tvWeatherState.text = currentWeather.weather.get(0).description

                        var sunsetTime = unixToTime(currentWeather.sys.sunset)
                        tvSunSetTime.text = sunsetTime

                        var sunriseTime = unixToTime(currentWeather.sys.sunrise)
                        tvSunRiseTime.text = sunriseTime

                        tvTownName.text = currentWeather.name

                        tvPressure.text = "" + currentWeather.main.pressure.toString() + " mbar"
                        if (currentWeather.main.pressure < 1000) {
                            tvPressureDesc.text = "Low Pressure"
                        } else {
                            if (currentWeather.main.pressure in 1001..1019) {
                                tvPressureDesc.text = "Moderate Pressure"
                            } else {
                                tvPressureDesc.text = "High Pressure"
                            }
                        }



                        tvHumidity.text = currentWeather.main.humidity.toString() + "%"
                        if (currentWeather.main.humidity in 0..29) {
                            tvHumidityDesc.text = "The air feels dry"
                        } else {
                            if (currentWeather.main.humidity in 30..60) {
                                tvHumidityDesc.text = "The air feels comfortable for most people"
                            } else {
                                tvHumidityDesc.text = "The air feels heavy and sticky"
                            }
                        }

                        tvVisibility.text = (currentWeather.visibility / 1000).toString()
                        if (currentWeather.visibility >= 10000) {
                            tvVisibilityDesc.text = "Perfectly clear view."
                        } else {
                            if (currentWeather.visibility in 1000..10000) {
                                tvVisibilityDesc.text = "Nice view."
                            } else {
                                if (currentWeather.visibility < 1000) {
                                    tvVisibilityDesc.text = "Poor view."
                                } else {
                                    if (currentWeather.visibility < 100) {
                                        tvVisibilityDesc.text = "Severely poor view."
                                    }
                                }
                            }
                        }

                    } else {
                        println("Nothing")
                    }
                }
            }
            homeViewModel.getWeatherForecast()
            lifecycleScope.launch {
                homeViewModel.dataForecast.collect { weatherForecast ->
                    if (weatherForecast != null) {
                        myDailyDetailsAdapter.submitList(
                            weatherForecast.list.subList(0, 7).toMutableList()
                        )
                        var weatherList = ArrayList<WeatherCasting>()
                        for (i in 0 until 40 step 7) {
                            println("i :" + i)
                            weatherList.add(weatherForecast.list.get(i))
                        }
                        myFiveDaysDetailsAdapter.submitList(weatherList)
                    }
                }
            }


        } else {
            println("Fragment is not added!")
        }
    }

    fun unixToTime(unixTime: Long): String {
        var date = Date(unixTime * 1000)
        var dateFormat = SimpleDateFormat("h:m a", Locale.US)
        return dateFormat.format(date)
    }
}