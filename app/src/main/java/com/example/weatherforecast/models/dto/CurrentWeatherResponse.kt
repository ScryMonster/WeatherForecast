package com.example.weatherforecast.models.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentWeatherResponse(
    @SerializedName("coord") val coordinates: Coordinate,
    @SerializedName("weather") val currentWeather:List<Weather>,
    @SerializedName("name") val cityName:String,
    @SerializedName("dt") val date:Long,
    @SerializedName("main") val weatherDetails:WeatherDetails,
    @SerializedName("wind") val windDetails: WindDetails,
    @SerializedName("visibility") val visibility:Long
) : Parcelable