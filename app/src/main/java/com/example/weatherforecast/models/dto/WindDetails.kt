package com.example.weatherforecast.models.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WindDetails(
    @SerializedName("speed") val speed:Double,
    @SerializedName("deg") val degree:Double
): Parcelable