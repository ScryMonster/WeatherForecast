package com.example.weatherforecast.models.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    @SerializedName("id") val id:Long,
    @SerializedName("main") val mainDescription:String,
    @SerializedName("description") val fullDescription:String,
    @SerializedName("icon") val icon:String
) : Parcelable