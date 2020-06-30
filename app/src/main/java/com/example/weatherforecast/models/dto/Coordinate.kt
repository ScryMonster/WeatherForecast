package com.example.weatherforecast.models.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Coordinate(
    @SerializedName("lat") val lat:Double,
    @SerializedName("lon") val lon:Double
) : Parcelable