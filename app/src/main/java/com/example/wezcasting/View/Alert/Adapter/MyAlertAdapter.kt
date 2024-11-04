package com.example.wezcasting.View.Alert.Adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wezcasting.Model.Alarm
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.Model.WeatherRepository
import com.example.wezcasting.R
import com.example.wezcasting.View.Alert.MyBroadcastReceiver
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MyAlertAdapter(var context: Context, var weatherAlarms: List<Alarm>, private var weatherDatabase: WeatherDatabase) : ListAdapter<Alarm, MyAlertAdapter.ViewHolder> (
    AlertDiffUtil()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.alarms_row, parent, false)
        val viewHolder: ViewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentAlarm = getItem(position)
        println("inside alarm adapter:" + weatherAlarms.size)

        holder.tvSavedTownName.text = currentAlarm.id.toString()
        holder.tvTime.text = unixToTime(currentAlarm.time)

        holder.btnRemove.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                weatherDatabase.alarmDao().deleteAlarm(currentAlarm)
            }
            val intent = Intent(context, MyBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,currentAlarm.id,intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            if(pendingIntent != null && alarmManager != null){
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    class ViewHolder(var convertView: View) : RecyclerView.ViewHolder(convertView){

        var tvSavedTownName : TextView = convertView.findViewById(R.id.tvSavedTownName)
        var tvTime : TextView = convertView.findViewById(R.id.tvSavedCurrentTownTime)
        var btnRemove : ImageButton = convertView.findViewById(R.id.imageButton3)
    }

    fun unixToTime(unixTime : Long) : String{
        var date = Date(unixTime)
        var dateFormat = SimpleDateFormat("h:00 a", Locale.US)
        return dateFormat.format(date)
    }

    fun nightAndDay(unixTime: Long) : String{
        var date = Date(unixTime * 1000)
        var time = SimpleDateFormat("H", Locale.US)
        return time.format(date)
    }
}