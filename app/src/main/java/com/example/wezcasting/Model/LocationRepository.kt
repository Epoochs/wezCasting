package com.example.wezcasting.Model
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationRepository(private val context: Context, var onLocationUpdates: OnLocationUpdates){
    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    private val REQUEST_LOCATION_CODE = 1001

    init {
        if (checkPermissions()) {
            Log.d("LocationRepo", "Permissions are granted")
            if (isLocationEnabled()) {
                getFreshLocation()
                Log.d("LocationRepo", "Location is enabled")
            } else {
                enableLocationService()
                Log.d("LocationRepo", "Enabling location service")
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_CODE
            )
            Log.d("LocationRepo", "Requesting permissions")
        }
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        val locationRequest = LocationRequest.Builder(10000).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }.build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                @SuppressLint("Range")
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val lastLocation = locationResult.lastLocation
                    if (lastLocation != null) {
                        longitude = lastLocation.longitude
                        latitude = lastLocation.latitude
                        onLocationUpdates.getCurrentWeather(lastLocation.latitude, lastLocation.longitude)
                        onLocationUpdates.getWeatherForecast(lastLocation.latitude, lastLocation.longitude)
                        Log.d("LocationRepo", "Location received: $latitude, $longitude")
                    } else {
                        Log.d("LocationRepo", "Location is null")
                    }
                }
            },
            Looper.getMainLooper()
        )
    }


    fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun enableLocationService() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}