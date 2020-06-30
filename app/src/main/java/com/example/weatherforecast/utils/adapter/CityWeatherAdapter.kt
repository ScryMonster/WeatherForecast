package com.example.weatherforecast.utils.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.models.dto.CityWeather
import com.example.weatherforecast.models.dto.WhatToDraw
import com.example.weatherforecast.models.enums.SearchAdapterState
import com.example.weatherforecast.utils.extensions.inflate
import com.example.weatherforecast.utils.kelvinToCelsius
import com.example.weatherforecast.utils.round
import kotlinx.android.synthetic.main.item_city_weather_layout.view.*

class CityWeatherAdapter : RecyclerView.Adapter<CityWeatherAdapter.CityWeatherViewHolder>(),
    SwipeableAdapter {

    private val mutableItems = arrayListOf<CityWeather>()

    private var state: SearchAdapterState = SearchAdapterState.IDLE

    private var clickListener: (CityWeather) -> Unit = {}
    private var onSaveClickListener: (CityWeather) -> Unit = {}
    private var onDeleteClickListener: (CityWeather) -> Unit = {}

    fun setOnClickListener(clickListener: (CityWeather) -> Unit) = apply {
        this.clickListener = clickListener
    }

    fun setOnSaveClickListener(clickListener: (CityWeather) -> Unit) = apply {
        this.onSaveClickListener = clickListener
    }

    fun setOnDeleteClickListener(clickListener: (CityWeather) -> Unit) = apply {
        this.onDeleteClickListener = clickListener
    }

    fun setSearchAdapterState(state: SearchAdapterState) {
        this.state = state
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityWeatherViewHolder =
        CityWeatherViewHolder(parent.inflate(R.layout.item_city_weather_layout, false))

    override fun onBindViewHolder(holder: CityWeatherViewHolder, position: Int) {
        holder.bind(mutableItems[position], clickListener)
    }

    override fun getItemCount() = mutableItems.size


    override fun onSwipe(position: Int) {
        val item = mutableItems[position]
        if (!item.isCurrent) {
            val isSaved = item.isSaved
            if (isSaved) onDeleteClickListener(item)
            else onSaveClickListener(item.also { it.isSaved = true })
        }
    }


    override fun whatToDraw(position: Int): WhatToDraw? = when {
        isShowingCurrentCity(position) -> null
        canBeDeleted(position) -> WhatToDraw.Delete
        canBeSaved(position) -> WhatToDraw.Save
        else -> null
    }

    private fun canBeDeleted(position: Int) =
        mutableItems[position].isSaved && state == SearchAdapterState.IDLE

    private fun isShowingCurrentCity(position: Int) = mutableItems[position].isCurrent

    private fun canBeSaved(position: Int) = !mutableItems[position].isSaved


    fun newSearchResponse(item: CityWeather) {
        val onlyCurrentWeatherList = mutableItems.filter { it.isCurrent }.toMutableList()
        onlyCurrentWeatherList.add(item)
        mutableItems.clear()
        mutableItems.addAll(onlyCurrentWeatherList)
        notifyDataSetChanged()
    }

    fun clearToCurrent() {
        val onlyCurrentWeatherList = mutableItems.filter { it.isCurrent }
        mutableItems.clear()
        mutableItems.addAll(onlyCurrentWeatherList)
        notifyDataSetChanged()
    }

    fun addItem(item: CityWeather) {
        val newList = mutableItems.toMutableList().apply { sortBy { it.isCurrent } }
        newList.add(item)
        mutableItems.clearAndAdd(newList.distinct())

        notifyItemInserted(mutableItems.indexOf(item))
    }

    fun addItems(items: List<CityWeather>) {
        mutableItems.clearAndAdd(items.apply { sortedBy { it.isCurrent } })

        notifyDataSetChanged()
    }

    fun removeItem(item: CityWeather) {
        val foundItem = mutableItems.firstOrNull { it == item }
        mutableItems.remove(foundItem)

        notifyDataSetChanged()
    }

    fun saveItem(item: CityWeather) {
        mutableItems.replaceAll {
            if (it.cityName == item.cityName) it.apply {
                isSaved = true
            } else it
        }
    }


    private fun <T> ArrayList<T>.clearAndAdd(list: List<T>) {
        clear()
        addAll(list)
    }


    class CityWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: CityWeather, clickListener: (CityWeather) -> Unit) {
            itemView.apply {
                container_searched_city.isVisible = true
                tv_searched_city_name.text = item.cityName
                tv_searched_temperature.text = context.getString(
                    R.string.min_max_temp,
                    item.weatherDetails.minTemperature.kelvinToCelsius().round(),
                    item.weatherDetails.maxTemperature.kelvinToCelsius().round()
                )
                tv_searched_city_weather_description.text = item.currentWeather[0].mainDescription
                setOnClickListener {
                    clickListener(item)
                }
            }
        }
    }
}