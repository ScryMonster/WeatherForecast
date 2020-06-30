package com.example.weatherforecast.models.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class WeatherDay(
    @SerializedName("dt") val date:Long,
    @SerializedName("sunrise") val sunriseTime:Long,
    @SerializedName("sunset") val  sunsetTime:Long,
    @SerializedName("temp") val temperature:Temperature,
    @SerializedName("pressure") val pressure:Double,
    @SerializedName("humidity") val humidity:Double,
    @SerializedName("speed") val windSpeed:Double,
    @SerializedName("weather") val weather:List<WeatherDescription>
) : Parcelable