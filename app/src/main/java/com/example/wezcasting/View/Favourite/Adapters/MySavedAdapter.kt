package com.example.wezcasting.View.Favourite.Adapters

import android.content.Context
import android.content.Intent
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
import com.example.wezcasting.View.Favourite.SavedActivity
import com.example.wezcasting.WeatherActivity
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MySavedAdapter(var context: Context, var weatherCasting: List<CurrentWeather>, var lang : String?, var unit : String?, var genUnit : String?) : ListAdapter<CurrentWeather, MySavedAdapter.ViewHolder> (
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

        println("currentWeatherForecast Temp unit: " + currentWeatherForecast.tempUnit)
        println("currentWeatherForecast Temp value:" + currentWeatherForecast.main.temp)

        println( "Fav " + unit)

        if (unit.equals("c")) {
            if (currentWeatherForecast.tempUnit.equals("c")) {

                holder.tvTimeTemp.text = currentWeatherForecast.main.temp.toInt().toString() + "°"
                holder.tvTownName.text = currentWeatherForecast.name
                holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                holder.tvTempMax.text =
                    "H:" + currentWeatherForecast.main.temp_max.toInt().toString() + "°"
                holder.tvTempMin.text =
                    "L:" + currentWeatherForecast.main.temp_min.toInt().toString() + "°"
            }else{
                if (currentWeatherForecast.tempUnit.equals("f")){
                    holder.tvTimeTemp.text = ((currentWeatherForecast.main.temp - 32) * 5/9).toInt().toString() + "°"
                    holder.tvTownName.text = currentWeatherForecast.name
                    holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                    holder.tvTempMax.text =
                        "H:" + ((currentWeatherForecast.main.temp_max - 32) * 5 / 9).toInt().toString() + "°"
                    holder.tvTempMin.text =
                        "L:" + ((currentWeatherForecast.main.temp_min - 32) * 5 / 9).toInt().toString() + "°"
                }else{
                    holder.tvTimeTemp.text = (currentWeatherForecast.main.temp - 273.15).toInt().toString() + "°"
                    holder.tvTownName.text = currentWeatherForecast.name
                    holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                    holder.tvTempMax.text =
                        "H:" + (currentWeatherForecast.main.temp_max - 273.15).toInt().toString() + "°"
                    holder.tvTempMin.text =
                        "L:" + (currentWeatherForecast.main.temp_min - 273.15).toInt().toString() + "°"
                }
            }
        }else{
            if (unit.equals("f")){
                if (currentWeatherForecast.tempUnit.equals("f")){
                    holder.tvTimeTemp.text = currentWeatherForecast.main.temp.toInt().toString() + "°"
                    holder.tvTownName.text = currentWeatherForecast.name
                    holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                    holder.tvTempMax.text =
                        "H:" + currentWeatherForecast.main.temp_max.toInt().toString() + "°"
                    holder.tvTempMin.text =
                        "L:" + currentWeatherForecast.main.temp_min.toInt().toString() + "°"
                }else{
                    if (currentWeatherForecast.tempUnit.equals("c")){
                        holder.tvTimeTemp.text = ((currentWeatherForecast.main.temp * 1.8) + 32).toInt().toString() + "°"
                        holder.tvTownName.text = currentWeatherForecast.name
                        holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                        holder.tvTempMax.text =
                            "H:" + ((currentWeatherForecast.main.temp_max * 1.8) + 32).toInt().toString() + "°"
                        holder.tvTempMin.text =
                            "L:" + ((currentWeatherForecast.main.temp_min * 1.8) + 32).toInt().toString() + "°"
                    }else{
                        holder.tvTimeTemp.text = ((currentWeatherForecast.main.temp - 273.15) * 1.8 + 32).toInt().toString() + "°"
                        holder.tvTownName.text = currentWeatherForecast.name
                        holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                        holder.tvTempMax.text =
                            "H:" + ((currentWeatherForecast.main.temp_max - 273.15) * 1.8 + 32).toInt().toString() + "°"
                        holder.tvTempMin.text =
                            "L:" + ((currentWeatherForecast.main.temp_min - 273.15) * 1.8 + 32).toInt().toString() + "°"
                    }
                }
            }else{
                if (currentWeatherForecast.tempUnit.equals("k")){
                    holder.tvTimeTemp.text = currentWeatherForecast.main.temp.toInt().toString() + "°"
                    holder.tvTownName.text = currentWeatherForecast.name
                    holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                    holder.tvTempMax.text =
                        "H:" + currentWeatherForecast.main.temp_max.toInt().toString() + "°"
                    holder.tvTempMin.text =
                        "L:" + currentWeatherForecast.main.temp_min.toInt().toString() + "°"
                }else{
                    if(currentWeatherForecast.tempUnit.equals("c")){
                        holder.tvTimeTemp.text = (currentWeatherForecast.main.temp + 273.15).toInt().toString() + "°"
                        holder.tvTownName.text = currentWeatherForecast.name
                        holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                        holder.tvTempMax.text =
                            "H:" + (currentWeatherForecast.main.temp_max + 273.15).toInt().toString() + "°"
                        holder.tvTempMin.text =
                            "L:" + (currentWeatherForecast.main.temp_min + 273.15).toInt().toString() + "°"
                    }else{
                        if (currentWeatherForecast.tempUnit.equals("f")){
                            holder.tvTimeTemp.text = ((currentWeatherForecast.main.temp - 32) * 5 / 9 + 273.15).toInt().toString() + "°"
                            holder.tvTownName.text = currentWeatherForecast.name
                            holder.tvWeatherDesc.text = currentWeatherForecast.weather.get(0).description
                            holder.tvTempMax.text =
                                "H:" + ((currentWeatherForecast.main.temp_max - 32) * 5 / 9 + 273.15).toInt().toString() + "°"
                            holder.tvTempMin.text =
                                "L:" + ((currentWeatherForecast.main.temp_min - 32) * 5 / 9 + 273.15).toInt().toString() + "°"
                        }
                    }
                }
            }
        }

        holder.convertView.setOnClickListener{
            val intent = Intent(context, WeatherActivity::class.java)
            intent.putExtra("Lat" ,currentWeatherForecast.coord.lat)
            intent.putExtra("Lon", currentWeatherForecast.coord.lon)
            intent.putExtra("id", currentWeatherForecast.id)
            intent.putExtra("AdapterLang", lang)
            intent.putExtra("AdapterUnit", unit)
            intent.putExtra("General Unit", genUnit)
            context.startActivity(intent)
        }
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