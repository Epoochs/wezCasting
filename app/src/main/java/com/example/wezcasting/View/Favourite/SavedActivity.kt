package com.example.wezcasting.View.Favourite

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.LocationRepository
import com.example.wezcasting.Model.OnLocationUpdates
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.Networking.WeatherClinet
import com.example.wezcasting.Networking.WeatherService
import com.example.wezcasting.R
import com.example.wezcasting.View.Favourite.Adapters.MySavedAdapter
import com.example.wezcasting.View.Favourite.ViewModel.SavedViewModel
import com.example.wezcasting.View.Favourite.ViewModel.SavedViewModelFactory
import com.example.wezcasting.View.Home.Adapter.MyDailyDetailsAdapter
import com.example.wezcasting.View.Home.ViewModel.HomeViewModel
import com.example.wezcasting.View.Home.ViewModel.HomeViewModelFactory
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.launch

class SavedActivity : AppCompatActivity(), OnLocationUpdates {

    lateinit var recyclerViewSaved: RecyclerView
    lateinit var mySavedAdapter: MySavedAdapter
    lateinit var savedLayoutManager: LinearLayoutManager

    lateinit var savedViewModel : SavedViewModel
    lateinit var savedViewModelFactory: SavedViewModelFactory

    lateinit var weatherService : WeatherService
    lateinit var weatherDatabase: WeatherDatabase
    lateinit var weatherRepository: WeatherRepository
    lateinit var locationRepository : LocationRepository

    var lang : String? = "en"
    var unit : String? = "metric"
    var genUnit : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_saved)

        supportActionBar?.hide()

        lang = intent.getStringExtra("langSaved")
        unit = intent.getStringExtra("unitSaved")
        genUnit = intent.getStringExtra("GenUnitSaved")

        println("savedLang: " + lang)
        println("savedUnit: " + unit)

        weatherService = WeatherClinet.weatherService
        weatherDatabase = WeatherDatabase.getInstance(this)
        weatherRepository = WeatherRepository.getInstance(weatherService,weatherDatabase)
        locationRepository = LocationRepository(this,this)

        savedViewModelFactory = SavedViewModelFactory(weatherRepository, 0.0, 0.0)
        savedViewModel = ViewModelProvider(this, savedViewModelFactory).get(SavedViewModel::class.java)

        init()

        lifecycleScope.launch {
            savedViewModel.savedWeatherData.collect{
                if (it != null) {
                    mySavedAdapter.submitList(it.toMutableList())
                }
            }
        }

    }

    fun init(){
        recyclerViewSaved = findViewById(R.id.rvSavedWeatherLoc)
        savedLayoutManager = LinearLayoutManager(this)
        savedLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewSaved.setLayoutManager(savedLayoutManager)
        mySavedAdapter = MySavedAdapter(this, emptyList(), lang, unit, genUnit)
        recyclerViewSaved.setAdapter(mySavedAdapter)
    }

    override fun getCurrentWeather(lat: Double, lon: Double) {
    }

    override fun getWeatherForecast(lat: Double, lon: Double) {
    }

    override fun getMycurrentLocation(lat: Double, lon: Double) {
    }
}