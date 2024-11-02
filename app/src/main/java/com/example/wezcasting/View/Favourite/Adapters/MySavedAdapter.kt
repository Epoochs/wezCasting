package com.example.wezcasting.View.Favourite.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wezcasting.Model.CurrentWeather
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.R
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MySavedAdapter(var context: Context, var weatherCasting: List<CurrentWeather>) : ListAdapter<CurrentWeather, MySavedAdapter.ViewHolder> (
    SavedDiffUtil()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.saved_weather_row, parent, false)
        val viewHolder: ViewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeatherForecast = getItem(position)

        holder.tvTimeTemp.text = currentWeatherForecast.main.temp.toInt().toString() + "°"
        holder.tvTownName.text = currentWeatherForecast.name
        holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
        holder.tvTempMax.text = "H:" + currentWeatherForecast.main.temp_max.toInt().toString() + "°"
        holder.tvTempMin.text = "L:" + currentWeatherForecast.main.temp_min.toInt().toString() + "°"
    }

    class ViewHolder(var convertView: View) : RecyclerView.ViewHolder(convertView){

        var tvTimeTemp : TextView = convertView.findViewById(R.id.tvSavedTownTemp)
        var tvTownName : TextView = convertView.findViewById(R.id.tvSavedTownName)
        var tvWeatherDesc : TextView = convertView.findViewById(R.id.tvSaveTownWeatherState)
        var tvTempMax : TextView = convertView.findViewById(R.id.tvSavedTownMaxTemp)
        var tvTempMin : TextView = convertView.findViewById(R.id.tvSaveTownMinTemp)
    }

    fun unixToTime(unixTime : Long) : String{
        var date = Date(unixTime * 1000)
        var dateFormat = SimpleDateFormat("h:00 a", Locale.US)
        return dateFormat.format(date)
    }

    fun nightAndDay(unixTime: Long) : String{
        var date = Date(unixTime * 1000)
        var time = SimpleDateFormat("H", Locale.US)
        return time.format(date)
    }

    override fun submitList(list: MutableList<CurrentWeather>?) {
        super.submitList(list)
    }
}