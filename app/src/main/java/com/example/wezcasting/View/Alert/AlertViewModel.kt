package com.example.wezcasting.View.Alert

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.db.AlarmDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel(private val alertDao: AlarmDao) : ViewModel() {

    private val _alerts = MutableStateFlow<List<Alarm>>(emptyList())
    val alert : StateFlow<List<Alarm>> = _alerts

    init {
        getAlarms()
    }

    fun addAlert(alert: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            alertDao.insertAlarm(alert)
            getAlarms()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleAllAlerts(context: Context, currentWeather: CurrentWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            alert.collect { alertList ->

                val currentTime = System.currentTimeMillis()

                val futureAlerts = alertList.filter { it.time > currentTime }

                futureAlerts.forEach { alert ->
                    val intent = Intent(context, MyBroadcastReceiver::class.java).apply {
                        putExtra("alertId", alert.id)
                        putExtra("weather", currentWeather.weather.get(0).description)
                        putExtra("Town", currentWeather.name)
                    }

                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        alert.id,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

                    alarmManager?.cancel(pendingIntent)

                    alarmManager?.setExact(
                        AlarmManager.RTC_WAKEUP,
                        alert.time,
                        pendingIntent
                    )
                }
            }
        }
    }

    fun getAlarms(){
        viewModelScope.launch {
            alertDao.getAllAlarms().collect{
                _alerts.value = it
                println("Size of alarms list" + it.size)
            }
        }
    }

    fun cancelAlarm(context: Context, alertId: Int){
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,alertId,intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if(pendingIntent != null && alarmManager != null){
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
