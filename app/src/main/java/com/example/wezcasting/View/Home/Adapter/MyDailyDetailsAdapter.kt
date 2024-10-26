package com.example.wezcasting.View.Home.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.R
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MyDailyDetailsAdapter(var context: Context, var weatherCasting: List<WeatherCasting>) : ListAdapter<WeatherCasting, MyDailyDetailsAdapter.ViewHolder> (
    HomeDiffUtil()
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.hourly_row, parent, false)
        val viewHolder: ViewHolder = ViewHolder(v)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentWeatherForecast = getItem(position)

        holder.tvTimeTemp.text = "" + currentWeatherForecast.main.temp.toString() + "°"
        holder.tvTime.text = unixToTime(currentWeatherForecast.dt)

        if (nightAndDay(currentWeatherForecast.dt).toInt() in 0 .. 6 || nightAndDay(currentWeatherForecast.dt).toInt() in 18 .. 24){
            if (currentWeatherForecast.weather.get(0).main == "Clear") {
                holder.imageView.setImageResource(R.drawable.moon)
            }else{
                if(currentWeatherForecast.weather.get(0).main == "Clouds"){
                    holder.imageView.setImageResource(R.drawable.cloud1)
                }else{
                    if (currentWeatherForecast.weather.get(0).main == "Snow"){
                        holder.imageView.setImageResource(R.drawable.snowflake)
                    }
                }
            }
        }else{
            holder.imageView.setImageResource(R.drawable.sun)
        }
    }

    class ViewHolder(var convertView: View) : RecyclerView.ViewHolder(convertView){

        var tvTimeTemp : TextView = convertView.findViewById(R.id.tvTimeTemp)
        var tvTime : TextView = convertView.findViewById(R.id.tvTime)
        var imageView : ImageView = convertView.findViewById(R.id.imgTimeState)
    }

    fun unixToTime(unixTime : Long) : String{
        var date = Date(unixTime * 1000)
        var dateFormat = SimpleDateFormat("h:m a", Locale.US)
        return dateFormat.format(date)
    }

    fun nightAndDay(unixTime: Long) : String{
        var date = Date(unixTime * 1000)
        var time = SimpleDateFormat("H", Locale.US)
        return time.format(date)
    }
}