package com.example.wezcasting.View.Alert

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.LocationRepository
import com.example.wezcasting.Model.OnLocationUpdates
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.Model.interfaces.WeatherLocalDataSourceImp
import com.example.wezcasting.Model.interfaces.WeatherRemoteDataSourceImp
import com.example.wezcasting.Networking.WeatherClinet
import com.example.wezcasting.Networking.WeatherService
import com.example.wezcasting.R
import com.example.wezcasting.View.Alert.Adapter.MyAlertAdapter
import com.example.wezcasting.View.Home.Adapter.MyDailyDetailsAdapter
import com.example.wezcasting.db.AlarmDao
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.launch
import java.util.Calendar

class AlertFragment : Fragment(), OnLocationUpdates {

    lateinit var btnAddAlarm : ImageButton
    lateinit var alertViewModel: AlertViewModel
    lateinit var alertViewModelFactory: AlertViewModelFactory
    lateinit var alertDao: AlarmDao
    lateinit var database: WeatherDatabase
    lateinit var locationRepository : LocationRepository
    lateinit var weatherService : WeatherService
    lateinit var weatherRepository: WeatherRepository
    lateinit var weatherDatabase: WeatherDatabase

    lateinit var recyclerViewAlert: RecyclerView
    lateinit var myAlertDetailsAdapter: MyAlertAdapter
    lateinit var alertLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherService = WeatherClinet.weatherService
        val weatherDatabase = WeatherDatabase.getInstance(requireActivity())

        val localDataSource = WeatherLocalDataSourceImp(weatherDatabase)
        val remoteDataSource = WeatherRemoteDataSourceImp(weatherService)
        weatherRepository = WeatherRepository.getInstance(localDataSource, remoteDataSource)

        btnAddAlarm = view.findViewById(R.id.btnAddAlert)

        btnAddAlarm.setOnClickListener{
            showDataTimePickers()
            locationRepository = LocationRepository(requireActivity(), this)
        }

        database = WeatherDatabase.getInstance(requireContext())
        alertDao = database.alarmDao()

        alertViewModelFactory = AlertViewModelFactory(alertDao)
        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)

        recyclerViewAlert = view.findViewById(R.id.rvSavedAlarms)
        alertLayoutManager = LinearLayoutManager(requireActivity())
        alertLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerViewAlert.setLayoutManager(alertLayoutManager)
        myAlertDetailsAdapter = MyAlertAdapter(requireActivity(), emptyList(), weatherDatabase)
        recyclerViewAlert.setAdapter(myAlertDetailsAdapter)

        alertViewModel.getAlarms()

        lifecycleScope.launch {
            alertViewModel.alert.collect{
                println("List alert in UI: " + it.size)
                myAlertDetailsAdapter.submitList(it.toMutableList())
            }
        }
    }

    private fun showDataTimePickers(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)

        val dataPickerDialog = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int) {
                val timePickerDialog = TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener{
                    override fun onTimeSet(view: TimePicker?, selectedHour: Int, selectedMinute: Int) {
                        calendar.set(selectedYear, selectedMonth, selectedDay)
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                        calendar.set(Calendar.MINUTE, selectedMinute)
                        alertViewModel.addAlert(Alarm(0,calendar.timeInMillis))
                    }
                }, hour, min, true)
                timePickerDialog.show()
            }
        }, year, month, day)
        dataPickerDialog.show()
    }

    override fun getCurrentWeather(lat: Double, lon: Double) {
        lifecycleScope.launch {
            weatherRepository.fetchCurrentWeather(lat, lon, "en", "metric").collect{ currentWeather ->
                if (currentWeather != null) {
                    alertViewModel.scheduleAllAlerts(requireContext(),currentWeather)
                }
            }
        }
    }

    override fun getWeatherForecast(lat: Double, lon: Double) {
    }

    override fun getMycurrentLocation(lat: Double, lon: Double) {
    }
}