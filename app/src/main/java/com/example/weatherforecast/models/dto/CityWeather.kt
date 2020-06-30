package com.example.weatherforecast.models.dto

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherforecast.database.convertors.WeatherListConvertor
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "CityWeatherTable")
data class CityWeather(
    @PrimaryKey var cityName: String,
    @Embedded var coordinates: Coordinate,
    @TypeConverters(WeatherListConvertor::class)
    var currentWeather: List<Weather>,
    var date: Long,
    @Embedded var weatherDetails: WeatherDetails,
    var isSaved: Boolean = false,
    var isCurrent: Boolean = false,
    @Embedded var windDetails: WindDetails,
    var visibility: Long
) : Parcelable