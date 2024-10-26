package com.example.wezcasting.View.Home.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.wezcasting.Model.WeatherCasting
import com.example.wezcasting.R
import com.example.wezcasting.Model.WeatherForecastResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MyFiveDaysDetailsAdapter(var context: Context, var weatherCasting: List<WeatherCasting>) : ListAdapter<WeatherCasting, MyFiveDaysDetailsAdapter.ViewHolder> (
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
}