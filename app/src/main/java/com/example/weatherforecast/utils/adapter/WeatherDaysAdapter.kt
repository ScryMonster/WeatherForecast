package com.example.weatherforecast.utils.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.models.dto.WeatherDay
import com.example.weatherforecast.utils.extensions.inflate
import com.example.weatherforecast.utils.kelvinToCelsius
import com.example.weatherforecast.utils.round
import kotlinx.android.synthetic.main.item_weather_forecast_layout.view.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class WeatherDaysAdapter : RecyclerView.Adapter<WeatherDaysAdapter.WeatherDaysViewHolder>() {

    private val mutableItems = arrayListOf<WeatherDay>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WeatherDaysViewHolder(
        parent.inflate(
            R.layout.item_weather_forecast_layout, false
        )
    )

    override fun onBindViewHolder(holder: WeatherDaysViewHolder, position: Int) {
        holder.bind(mutableItems[position])
    }

    override fun getItemCount(): Int = mutableItems.size



    fun addItems(items:List<WeatherDay>){
        mutableItems.clearAndAdd(items)
        notifyDataSetChanged()
    }

    private fun <T> ArrayList<T>.clearAndAdd(list: List<T>) {
        clear()
        addAll(list)
    }

    class WeatherDaysViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: WeatherDay) {
            itemView.apply {
                tv_forecast_temperature.text = context.getString(R.string.celcium_sign,item.temperature.day.kelvinToCelsius().round())

                val sdf = SimpleDateFormat("EEE, d MMM")
                val dayString: String = sdf.format(Date(item.date * 1000))
                tv_forecast_day_of_the_week.text = dayString
                tv_forecast_description.text = item.weather[0].main
            }
        }
    }
}