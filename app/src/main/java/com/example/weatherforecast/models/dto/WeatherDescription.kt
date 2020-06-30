package com.example.weatherforecast.models.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherDescription(
    @SerializedName("id")val id:Long,
    @SerializedName("main") val main: String,
    @SerializedName("description")val description: String,
    @SerializedName("icon") val icon: String
) : Parcelable