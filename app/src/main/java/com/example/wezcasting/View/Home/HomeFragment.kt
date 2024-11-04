package com.example.wezcasting.View.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.Networking.ConnectivityRepository
import com.example.wezcasting.Model.LocationRepository
import com.example.wezcasting.Model.OnLocationUpdates
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.Networking.WeatherClinet
import com.example.wezcasting.Networking.WeatherService
import com.example.wezcasting.R
import com.example.wezcasting.View.Home.Adapter.MyDailyDetailsAdapter
import com.example.wezcasting.View.Home.Adapter.MyFiveDaysDetailsAdapter
import com.example.wezcasting.View.Home.ViewModel.HomeViewModel
import com.example.wezcasting.View.Home.ViewModel.HomeViewModelFactory
import com.example.wezcasting.HomeSettingsSharedVM
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.db.WeatherDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale


class HomeFragment : Fragment() , OnLocationUpdates {

    var lat : Double = 0.0
    var lon : Double = 0.0

    var currentWeather : CurrentWeather? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    /* Recycle views and Adapters for Hourly details */
    lateinit var recyclerViewHourlyDetails: RecyclerView
    lateinit var myDailyDetailsAdapter: MyDailyDetailsAdapter
    lateinit var hourlyLayoutManager: LinearLayoutManager

    /* Recycle views and Adapters for five days */
    lateinit var recycleViewFiveDays : RecyclerView
    lateinit var myFiveDaysDetailsAdapter: MyFiveDaysDetailsAdapter
    lateinit var fiveLinearLayoutManager: LinearLayoutManager


    /* Weather fetching and data Representation */
    lateinit var weatherService : WeatherService
    lateinit var weatherRepository: WeatherRepository
    lateinit var locationRepository : LocationRepository
    lateinit var homeViewModel: HomeViewModel
    lateinit var weatherDatabase: WeatherDatabase

    /*SharedViewModel between Home and Setting fragments */
    lateinit var sharedVM: HomeSettingsSharedVM

    /* View Components */
    lateinit var tvTownCurrentTemp : TextView
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
    var connectionRestore = true

    /* Units */
    lateinit var tvVisibilityUnit : TextView
    lateinit var tvHumidityUnit : TextView
    lateinit var tvTempUnit : TextView
    lateinit var tvWindSpeedUnit : TextView
    lateinit var tvWindGustUnit : TextView
    lateinit var tvPressureUnit : TextView

    lateinit var connectivityRepository: ConnectivityRepository

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
        connectivityRepository = ConnectivityRepository(requireContext())
        val connection : LiveData<Boolean> = connectivityRepository.getIsConnected()

        connection.observe(requireActivity(), Observer{check ->
            if (check){
                if (connectionRestore) {
                    println("Connected")
                    weatherService = WeatherClinet.weatherService
                    weatherDatabase = WeatherDatabase.getInstance(requireActivity())
                    weatherRepository = WeatherRepository.getInstance(weatherService,weatherDatabase)
                    locationRepository = LocationRepository(requireActivity(), this)
                }else{
                    println("Connection Restored")
                    Snackbar.make(view, "Connection Restored", Snackbar.LENGTH_SHORT).show()
                    weatherService = WeatherClinet.weatherService
                    weatherDatabase = WeatherDatabase.getInstance(requireActivity())
                    weatherRepository = WeatherRepository.getInstance(weatherService,weatherDatabase)
                    locationRepository = LocationRepository(requireActivity(), this)
                    connectionRestore = true
                }
            }else{
                println("Not connected")
                connectionRestore = false
                Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                weatherService = WeatherClinet.weatherService
                weatherDatabase = WeatherDatabase.getInstance(requireActivity())
                weatherRepository = WeatherRepository.getInstance(weatherService,weatherDatabase)

            }
        })

     /*   weatherService = WeatherClinet.weatherService
        locationRepository = LocationRepository(requireActivity(),this)*/



        init(view)
    }

    override fun getCurrentWeather(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon

        println("Current Weather : " + lat)
        println("Current Weather : " + lon)

        // isAdded is used to check whether the fragment is added to the activity or not.
        if(isAdded) {

            sharedVM = ViewModelProvider(requireActivity()).get(HomeSettingsSharedVM::class.java)

            sharedVM.lang.observe(viewLifecycleOwner, Observer { lang ->
                println("Shared Language: " + lang)

                sharedVM.tempUnit.observe(viewLifecycleOwner, Observer { tempUnit ->
                    println("Shared TempUnit: " + tempUnit)

                    sharedVM.windUnit.observe(viewLifecycleOwner, Observer { windUnit ->
                        println("Shared WindUnit: " + windUnit)
                        sharedVM.unit.observe(viewLifecycleOwner, Observer { unit ->

                            val factory = HomeViewModelFactory(weatherRepository, lat, lon, lang, unit)
                            homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

                            homeViewModel.getCurrentWeather()

                            lifecycleScope.launch {
                                homeViewModel.data.collect { currentWeather ->
                                    if (currentWeather != null) {
                                        if(tempUnit.equals("c")) {
                                            tvTownCurrentTemp.text = currentWeather.main.temp.toInt().toString()
                                            tvTempUnit.text = "°C"

                                            /* Highest and Lowest Temp */
                                            tvHighestTemp.text = "H:" + currentWeather.main.temp_max.toInt() + "°"
                                            tvLowestTemp.text = "L:" + currentWeather.main.temp_min.toInt() + "°"

                                            /* Feels-like Temp */
                                            tvFeelsLike.text = "" + currentWeather.main.feels_like.toInt().toString() + "°"
                                            if (currentWeather.main.feels_like == currentWeather.main.temp) {
                                                tvFeelsLikeDesc.text = "it feels exactly the same"
                                            } else {
                                                if (currentWeather.main.feels_like > currentWeather.main.temp) {
                                                    tvFeelsLikeDesc.text = "it feels hotter"
                                                } else {
                                                    tvFeelsLikeDesc.text = "it feels cooler"
                                                }
                                            }
                                        }else{
                                            if(tempUnit.equals("f")){
                                                var currentTemp = ((currentWeather.main.temp * 1.8) + 32).toInt()
                                                tvTownCurrentTemp.text = currentTemp.toString()
                                                tvTempUnit.text = "°F"

                                                var highTemp_f = ((currentWeather.main.temp_max * 1.8) + 32).toInt()
                                                var lowTemp_f = ((currentWeather.main.temp_min * 1.8) + 32).toInt()
                                                tvHighestTemp.text = "H:" + highTemp_f + "°"
                                                tvLowestTemp.text = "L:" + lowTemp_f + "°"

                                                /* Feels-like Temp */
                                                var feelLikeTemp_f = ((currentWeather.main.feels_like * 1.8) + 32).toInt()
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
                                            }else{
                                                var currentTemp = (currentWeather.main.temp + 273.15).toInt()
                                                tvTownCurrentTemp.text = currentTemp.toString()
                                                tvTempUnit.text = "°K"

                                                tvHighestTemp.text = "H:" + (currentWeather.main.temp_max + 273.15).toInt() + "°"
                                                tvLowestTemp.text = "L:" + (currentWeather.main.temp_min + 273.15).toInt() + "°"

                                                /* Feels-like Temp */
                                                var feelLikeTemp_k = (currentWeather.main.feels_like + 273.15).toInt()
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
                                            }
                                        }

                                        if(windUnit.equals("km")) {
                                            var windSpeed = currentWeather.wind.speed * 3.6
                                            var windGust = currentWeather.wind.gust * 3.6
                                            tvGust.text = String.format("%.2f KM/H", windGust)
                                            tvSpeed.text = String.format("%.2f KM/H", windSpeed)
                                        }else{
                                            var windSpeed = currentWeather.wind.speed * 2.237
                                            var windGust = currentWeather.wind.gust * 2.237
                                            tvGust.text = String.format("%.2f MPH", windGust)
                                            tvSpeed.text = String.format("%.2f MPH", windSpeed)
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
                                    weatherRepository.upsert(currentWeather)
                                    homeViewModel.getWeatherForecast()

                                    lifecycleScope.launch {
                                        homeViewModel.dataForecast.collect { weatherForecast ->
                                            if (weatherForecast != null) {
                                                myDailyDetailsAdapter.submitList(weatherForecast.list.subList(0, 7).toMutableList())
                                                var weatherList = ArrayList<WeatherCasting>()
                                                for (i in 0 until 40 step 7) {
                                                    println("i :" + i)
                                                    weatherList.add(weatherForecast.list.get(i))
                                                }
                                                myFiveDaysDetailsAdapter.submitList(weatherList)
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    })
                })
            })
        }else{
            println("Fragment is not added!")
        }
    }

    override fun getWeatherForecast(lat: Double, lon: Double) {
     /*   this.lat = lat
        this.lon = lon

        if(isAdded) {

            sharedVM = ViewModelProvider(requireActivity()).get(HomeSettingsSharedVM::class.java)

            sharedVM.lang.observe(requireActivity(), Observer {lang ->

                sharedVM.unit.observe(requireActivity(), Observer { unit ->
                    println(unit)
                    val factory = HomeViewModelFactory(weatherRepository, lat, lon, lang, unit)
                    homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

                    homeViewModel.getWeatherForecast()

                    lifecycleScope.launch {
                        homeViewModel.dataForecast.collect { weatherForecast ->
                            if (weatherForecast != null) {
                                myDailyDetailsAdapter.submitList(weatherForecast.list.subList(0, 7).toMutableList())
                                var weatherList = ArrayList<WeatherCasting>()
                                for (i in 0 until 40 step 7) {
                                    println("i :" + i)
                                    weatherList.add(weatherForecast.list.get(i))
                                }
                                myFiveDaysDetailsAdapter.submitList(weatherList)
                            }
                        }
                    }

                })
            })


        }*/
    }

    override fun getMycurrentLocation(lat: Double, lon: Double) {
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

        /* Units */
        tvVisibilityUnit = view.findViewById(R.id.tvVisibilityUnit)

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


        recycleViewFiveDays = view.findViewById(R.id.rvWeatherForecasting)
        fiveLinearLayoutManager = LinearLayoutManager(requireActivity())
        fiveLinearLayoutManager.orientation = RecyclerView.VERTICAL
        recycleViewFiveDays.layoutManager = fiveLinearLayoutManager
        myFiveDaysDetailsAdapter = MyFiveDaysDetailsAdapter(requireActivity(), emptyList())
        recycleViewFiveDays.adapter = myFiveDaysDetailsAdapter
        val divider =
            DividerItemDecoration(recycleViewFiveDays.getContext(), DividerItemDecoration.VERTICAL)
        recycleViewFiveDays.addItemDecoration(divider)
    }

    fun unixToTime(unixTime : Long) : String{
        var date = Date(unixTime * 1000)
        var dateFormat = SimpleDateFormat("h:m a", Locale.US)
        return dateFormat.format(date)
    }
}