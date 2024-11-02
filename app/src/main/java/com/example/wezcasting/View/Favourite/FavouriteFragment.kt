package com.example.wezcasting.View.Favourite

import android.app.AlertDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.LocationRepository
import com.example.wezcasting.Model.OnLocationUpdates
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.Networking.WeatherClinet
import com.example.wezcasting.Networking.WeatherService
import com.example.wezcasting.View.Favourite.ViewModel.FavoriteViewModel
import com.example.wezcasting.View.Favourite.ViewModel.FavoriteViewModelFactory
import com.example.wezcasting.View.Home.ViewModel.HomeViewModel
import com.example.wezcasting.View.Home.ViewModel.HomeViewModelFactory
import com.example.wezcasting.db.WeatherDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
//import org.osmdroid.views.overlay.Marker
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment(), OnMapReadyCallback, OnLocationUpdates {

    private lateinit var googleMapView: GoogleMap
    lateinit var locationRepository : LocationRepository
    lateinit var searchView : SearchView
    lateinit var geocoder: Geocoder
    lateinit var myMark : Marker
    lateinit var myMarker : Marker
    private lateinit var myLocationButton: ImageButton
    private lateinit var mySavesButton: ImageButton
    lateinit var  myCurrentLoc : LatLng
    var count = true
    var dioShow = false

    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    lateinit var weatherService : WeatherService
    lateinit var weatherRepository: WeatherRepository
    lateinit var weatherDatabase: WeatherDatabase
    var currentWeather1 : CurrentWeather? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.wezcasting.R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherService = WeatherClinet.weatherService
        weatherDatabase = WeatherDatabase.getInstance(requireActivity())
        weatherRepository = WeatherRepository.getInstance(weatherService,weatherDatabase)

        favoriteViewModelFactory = FavoriteViewModelFactory(weatherRepository, 0.0, 0.0)
        favoriteViewModel = ViewModelProvider(this, favoriteViewModelFactory).get(FavoriteViewModel::class.java)


        val mapFragment = childFragmentManager.findFragmentById(com.example.wezcasting.R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        searchView = view.findViewById(com.example.wezcasting.R.id.svGetWeather)
        myLocationButton = view.findViewById(com.example.wezcasting.R.id.my_location_button)
        mySavesButton = view.findViewById(com.example.wezcasting.R.id.my_save_button)
        geocoder = Geocoder(requireContext())

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != null) {
                    // Perform search
                    googleMapView.clear()
                    val location = query
                    var address = geocoder.getFromLocationName(location,1)
                    println("Searching for: $query")
                    var latLin = address?.get(0)
                        ?.let { LatLng(address.get(0).latitude, it.longitude) }
                    latLin?.let {
                        MarkerOptions().position(it).title("You are here")
                    }?.let { googleMapView.addMarker(it) }
                    latLin?.let { CameraUpdateFactory.newLatLng(it) }
                        ?.let { googleMapView.moveCamera(it) }

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle the text change event
                // This is useful for real-time search suggestions
                if (newText != null) {
                    // Update suggestions or perform a real-time search
                    println("Query changed: $newText")
                }
                return true
            }
        })

        myLocationButton.setOnClickListener{
            var myMarker = googleMapView.addMarker(MarkerOptions().position(myCurrentLoc).title("You are here").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
            ))
            googleMapView.moveCamera(CameraUpdateFactory.newLatLng(myCurrentLoc))
        }

        mySavesButton.setOnClickListener{
            val intent = Intent(requireContext(), SavedActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapView = googleMap
        locationRepository = LocationRepository(requireActivity(),this)
        googleMapView.uiSettings.isZoomControlsEnabled = true
        googleMapView.setOnMapClickListener(){location ->
            dioShow = true
            if(!myMark.equals(null)) {
                myMark.remove()
            }
            var latlin = LatLng(location.latitude, location.longitude)
            println("latlin" + latlin.toString())
            myMark = googleMapView.addMarker(MarkerOptions().position(latlin))!!
            getCurrentLocationWeather(location.latitude, location.longitude)
        }
    }

    override fun getCurrentWeather(lat: Double, lon: Double) {
        //showLocationDetailsDialog()

    }

    override fun getWeatherForecast(lat: Double, lon: Double) {
    }

    override fun getMycurrentLocation(lat: Double, lon: Double) {
        myCurrentLoc = LatLng(lat,lon)
        if(count) {
            myMarker = googleMapView.addMarker(
                MarkerOptions().position(myCurrentLoc).title("You are here").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
                )
            )!!
            googleMapView.moveCamera(CameraUpdateFactory.newLatLng(myCurrentLoc))
            myMark = myMarker
            //myMark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            count = false
        }
    }

    fun showLocationDetailsDialog(locationName: String, currentWeatherDesc: String, currentTemp : String, currentHighTemp:String
                                  ,currentLowTemp:String) {
        // Inflate the dialog layout
        val dialogView = layoutInflater.inflate(com.example.wezcasting.R.layout.popup_details, null)

        // Reference the views in the layout
        val tvLocationName = dialogView.findViewById<TextView>(com.example.wezcasting.R.id.tvLocationName)
        val tvCurrentWeather = dialogView.findViewById<TextView>(com.example.wezcasting.R.id.tvCurrentWeather)
        val tvCurrentTemp = dialogView.findViewById<TextView>(com.example.wezcasting.R.id.tvCurrentTemp)
        val tvCurrentLowTemp = dialogView.findViewById<TextView>(com.example.wezcasting.R.id.tvCurrentLowTemp)
        val tvCurrentHighTemp = dialogView.findViewById<TextView>(com.example.wezcasting.R.id.tvCurrentHighTemp)

        // Set the text
        tvLocationName.text = locationName
        tvCurrentWeather.text = currentWeatherDesc
        tvCurrentTemp.text = currentTemp
        tvCurrentHighTemp.text = currentHighTemp
        tvCurrentLowTemp.text = currentLowTemp

        // Create and show the dialog
        if(dioShow) {
            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    currentWeather1?.let { favoriteViewModel.upsert(it) }
                }
                .create()
                .show()
            dioShow = false
        }
    }

    fun getCurrentLocationWeather(lat: Double, lon: Double){
        println("getCurrentWeather " + lat)

        favoriteViewModel.updateCoordinates(lat,lon)
        favoriteViewModel.getCurrentWeather()

        lifecycleScope.launch {
            favoriteViewModel.data.collect{currenWeather->
                if(currenWeather != null){
                    currentWeather1 = currenWeather
                    println(currenWeather.name)
                    showLocationDetailsDialog(currenWeather.name,currenWeather.weather.get(0).description,currenWeather.main.temp.toString(),currenWeather.main.temp_max.toString(),currenWeather.main.temp_min.toString())
                }
            }
        }
    }
}