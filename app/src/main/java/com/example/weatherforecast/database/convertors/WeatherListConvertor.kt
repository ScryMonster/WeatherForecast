package com.example.weatherforecast.database.convertors

import androidx.room.TypeConverter
import com.example.weatherforecast.models.dto.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class WeatherListConvertor {



    private val weatherType: Type = object : TypeToken<List<Weather?>?>() {}.type


    @TypeConverter
    fun fromList(weather:List<Weather>):String = Gson().toJson(weather,weatherType)

    @TypeConverter
    fun toList(weather: String):List<Weather> = Gson().fromJson(weather, weatherType) as List<Weather>

}