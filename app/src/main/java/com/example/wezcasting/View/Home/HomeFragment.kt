package com.example.wezcasting.View.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wezcasting.Model.LocationRepository
import com.example.wezcasting.Model.OnLocationUpdates
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.Networking.WeatherClinet
import com.example.wezcasting.Networking.WeatherService
import com.example.wezcasting.R
import com.example.wezcasting.View.Home.Adapter.MyDailyDetailsAdapter
import com.example.wezcasting.View.Home.ViewModel.HomeViewModel
import com.example.wezcasting.View.Home.ViewModel.HomeViewModelFactory
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() , OnLocationUpdates {

    var lat : Double = 0.0
    var lon : Double = 0.0

    /* Recycle views and Adapters */
    lateinit var recyclerViewHourlyDetails: RecyclerView
    lateinit var myDailyDetailsAdapter: MyDailyDetailsAdapter
    lateinit var hourlyLayoutManager: LinearLayoutManager


    /* Weather fetching and data Representation */
    lateinit var weatherService : WeatherService
    lateinit var weatherRepository: WeatherRepository
    lateinit var locationRepository : LocationRepository
    lateinit var homeViewModel: HomeViewModel
    lateinit var weatherDatabase: WeatherDatabase

    /* View Components */
    lateinit var tvTownCurrentTemp : TextView
    lateinit var tvTempUnit : TextView
    lateinit var tvWeatherState : TextView
    lateinit var tvSunSetTime : TextView
    lateinit var tvSunRiseTime : TextView
    lateinit var tvHighestTemp : TextView
    lateinit var tvLowestTemp : TextView
    lateinit var tvTownName : TextView
    lateinit var tvVisibility : TextView
    lateinit var tvVisibilityDesc : TextView
    lateinit var tvHumidity : TextView
    lateinit var tvHumidityDesc : TextView
    lateinit var tvFeelsLike : TextView
    lateinit var tvFeelsLikeDesc : TextView
    lateinit var tvPressure : TextView
    lateinit var tvPressureDesc : TextView
    lateinit var tvSpeed : TextView
    lateinit var tvGust : TextView

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherService = WeatherClinet.weatherService
        weatherDatabase = WeatherDatabase.getInstance(requireActivity())
        weatherRepository = WeatherRepository(weatherService,weatherDatabase)
        locationRepository = LocationRepository(requireActivity(),this)

        init(view)
    }

    override fun getCurrentWeather(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon

        println("Current Weather : " + lat)
        println("Current Weather : " + lon)

        val factory = HomeViewModelFactory(weatherRepository,lat,lon)
        homeViewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)

         homeViewModel.getCurrentWeather()

          lifecycleScope.launch {
              homeViewModel.data.collect{ currentWeather ->
                  if (currentWeather != null) {
                      tvTownCurrentTemp.text = currentWeather.main.temp.toString()
                      tvTempUnit.text = "°C"

                      var windSpeed = currentWeather.wind.speed * 3.6
                      var windGust = currentWeather.wind.gust * 3.6
                      tvGust.text = String.format("%.2f KM/H", windGust)
                      tvSpeed.text = String.format("%.2f KM/H", windSpeed)

                      tvWeatherState.text = currentWeather.weather.get(0).description

                      var sunsetTime = unixToTime(currentWeather.sys.sunset)
                      tvSunSetTime.text = sunsetTime

                      var sunriseTime = unixToTime(currentWeather.sys.sunrise)
                      tvSunRiseTime.text = sunriseTime

                      tvTownName.text = currentWeather.name
                      tvHighestTemp.text = "H:" + currentWeather.main.temp_max + "°"
                      tvLowestTemp.text = "L:" + currentWeather.main.temp_min + "°"

                      tvPressure.text = "" + currentWeather.main.pressure.toString() + " mbar"
                      if(currentWeather.main.pressure < 1000){
                          tvPressureDesc.text = "Low Pressure"
                      }else{
                          if (currentWeather.main.pressure in 1001..1019){
                              tvPressureDesc.text = "Moderate Pressure"
                          }else{
                              tvPressureDesc.text = "High Pressure"
                          }
                      }

                      tvFeelsLike.text = "" + currentWeather.main.feels_like.toString() + "°"
                      if(currentWeather.main.feels_like == currentWeather.main.temp){
                          tvFeelsLikeDesc.text = "it feels exactly the same"
                      }else{
                          if (currentWeather.main.feels_like > currentWeather.main.temp){
                              tvFeelsLikeDesc.text = "it feels hotter"
                          }else{
                              tvFeelsLikeDesc.text = "it feels cooler"
                          }
                      }

                      tvHumidity.text = currentWeather.main.humidity.toString() + "%"
                      if (currentWeather.main.humidity in 0 .. 29){
                          tvHumidityDesc.text = "The air feels dry"
                      }else{
                          if (currentWeather.main.humidity in 30 .. 60){
                              tvHumidityDesc.text = "The air feels comfortable for most people"
                          }else{
                              tvHumidityDesc.text = "The air feels heavy and sticky"
                          }
                      }

                      tvVisibility.text = (currentWeather.visibility / 1000).toString() + " KM"
                      if (currentWeather.visibility >= 10000){
                          tvVisibilityDesc.text = "Perfectly clear view."
                      }else
                      {
                          if (currentWeather.visibility in 1000 .. 10000){
                              tvVisibilityDesc.text = "Nice view."
                          }else{
                              if (currentWeather.visibility < 1000){
                                  tvVisibilityDesc.text = "Poor view."
                              }else{
                                  if (currentWeather.visibility < 100){
                                      tvVisibilityDesc.text = "Severely poor view."
                                  }
                              }
                          }
                      }

                  }else{
                      println("Nothing")
                  }
                  weatherRepository.upsert(currentWeather)
              }
          }
    }

    override fun getWeatherForecast(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon

        val factory = HomeViewModelFactory(weatherRepository,lat,lon)
        homeViewModel = ViewModelProvider(this,factory).get(HomeViewModel::class.java)

        homeViewModel.getWeatherForecast()

        lifecycleScope.launch {
            homeViewModel.dataForecast.collect{weatherForecast ->
                if (weatherForecast != null){
                    myDailyDetailsAdapter.submitList(weatherForecast.list.subList(0,7))
                }
            }
        }
    }

    fun init(view: View){
        tvSpeed = view.findViewById(R.id.tvSpeed)
        tvGust = view.findViewById(R.id.tvGust)
        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvPressure = view.findViewById(R.id.tvPressure)
        tvVisibility = view.findViewById(R.id.tvVisibility)
        tvFeelsLike = view.findViewById(R.id.tvFeelsLike)
        tvHighestTemp = view.findViewById(R.id.tvHighestTemp)
        tvLowestTemp = view.findViewById(R.id.tvLowestTemp)
        tvWeatherState = view.findViewById(R.id.tvWeatherState)
        tvSunRiseTime = view.findViewById(R.id.tvSunRiseTime)
        tvSunSetTime = view.findViewById(R.id.tvSunSetTime)
        tvTempUnit = view.findViewById(R.id.tvTempUnit)
        tvTownName = view.findViewById(R.id.tvTownName)
        tvTownCurrentTemp = view.findViewById(R.id.tvTownCurrentTemp)

        tvPressureDesc = view.findViewById(R.id.tvPressureDesc)
        tvVisibilityDesc = view.findViewById(R.id.tvVisibilityDesc)
        tvFeelsLikeDesc = view.findViewById(R.id.tvFeelsLikeDesc)
        tvHumidityDesc = view.findViewById(R.id.tvHumidityDesc)

        recyclerViewHourlyDetails = view.findViewById(R.id.rvHourDetails)
        hourlyLayoutManager = LinearLayoutManager(requireActivity())
        hourlyLayoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerViewHourlyDetails.setLayoutManager(hourlyLayoutManager)
        myDailyDetailsAdapter = MyDailyDetailsAdapter(requireActivity(), emptyList())
        recyclerViewHourlyDetails.setAdapter(myDailyDetailsAdapter)
    }

    fun unixToTime(unixTime : Long) : String{
        var date = Date(unixTime * 1000)
        var dateFormat = SimpleDateFormat("h:m a", Locale.US)
        return dateFormat.format(date)
    }
}