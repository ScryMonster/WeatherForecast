package com.example.weatherforecast.models.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDetails(
    @SerializedName("temp") val temperature:Double,
    @SerializedName("temp_min") val minTemperature:Double,
    @SerializedName("temp_max") val maxTemperature:Double,
    @SerializedName("pressure") val pressure:Double,
    @SerializedName("humidity") val humidity:Double
) : Parcelable