package com.example.wezcasting.View.Home.Adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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
        val v = inflater.inflate(R.layout.five_days_row, parent, false)
        val viewHolder: ViewHolder = ViewHolder(v)

        return viewHolder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentWeatherForecast = getItem(position)

        holder.tvDay.text = unixToDate(currentWeatherForecast.dt)

        if (currentWeatherForecast.weather.get(0).main == "Clear"){
            holder.imageView.setImageResource(R.drawable.sun)
        }else{
            if (currentWeatherForecast.weather.get(0).main == "Clouds"){
                holder.imageView.setImageResource(R.drawable.clear_sky)
            }else{
                if (currentWeatherForecast.weather.get(0).main == "Snow"){
                    holder.imageView.setImageResource(R.drawable.snowflake)
                }
            }
        }

        holder.tvMinTemp.text = "" + currentWeatherForecast.main.tempMin.toInt().toString() + "°"
        holder.tvMaxTemp.text = "" + currentWeatherForecast.main.tempMax.toInt().toString() + "°"

        var currentTempMap = (currentWeatherForecast.main.temp - currentWeatherForecast.main.tempMin).toInt()
        var maxTempMap = (currentWeatherForecast.main.tempMax - currentWeatherForecast.main.tempMin).toInt()
        var currentTempPerc : Int
        if (maxTempMap > 0) {
            currentTempPerc = (currentTempMap / maxTempMap) * 100
        }else{
            currentTempPerc = 0
        }

        println("currentTempMap: " + currentTempMap)
        println("maxTempMap: " + maxTempMap)
        println("currentTempPerc" + currentTempPerc)


        holder.pbCurrentTemp.max = currentWeatherForecast.main.tempMax.toInt()
     //   holder.pbCurrentTemp.min = currentWeatherForecast.main.tempMin.toInt()
        if (currentTempPerc > 80) {
            holder.pbCurrentTemp.progressTintList =
                ContextCompat.getColorStateList(context, R.color.red)
        }else{
            if (currentTempPerc in 60..80){
                holder.pbCurrentTemp.progressTintList =
                    ContextCompat.getColorStateList(context, R.color.orangeRed)
            }else{
                if(currentTempPerc in 40..59){
                    holder.pbCurrentTemp.progressTintList =
                        ContextCompat.getColorStateList(context, R.color.yellowOrange)
                }else{
                    if (currentTempPerc in 30..39){
                        holder.pbCurrentTemp.progressTintList =
                            ContextCompat.getColorStateList(context, R.color.yellowGreen)
                    }else{
                        holder.pbCurrentTemp.progressTintList =
                            ContextCompat.getColorStateList(context, R.color.brightGreen)
                    }
                }
            }
        }
        holder.pbCurrentTemp.progress = currentTempPerc
    }

    class ViewHolder(var convertView: View) : RecyclerView.ViewHolder(convertView){

        var tvDay : TextView = convertView.findViewById(R.id.tvDay)
        var imageView : ImageView = convertView.findViewById(R.id.imageView3)
        var tvMinTemp : TextView = convertView.findViewById(R.id.tvMinTemp)
        var tvMaxTemp : TextView = convertView.findViewById(R.id.tvMaxTemp)
        var pbCurrentTemp : ProgressBar = convertView.findViewById(R.id.pbCurrentTemp)
    }

    fun unixToDate(unixTime : Long) : String{
        var date = Date(unixTime * 1000)
        var dateFormat = SimpleDateFormat("E", Locale.US)
        return dateFormat.format(date)
    }
}