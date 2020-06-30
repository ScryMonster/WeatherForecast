package com.example.weatherforecast.models.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Forecast(
    val coordinate: Coordinate,
    val cityName: String,
    val currentWeather: List<Weather>,
    val weatherDetails: WeatherDetails,
    val windDetails: WindDetails,
    val visibility: Long
) : Parcelable