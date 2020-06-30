package com.example.weatherforecast.utils

import java.text.DecimalFormat


fun Double.kelvinToCelsius():Double = this - 273.15

fun Double.round() = DecimalFormat("####0.0").format(this)

fun Long.metresToKm() = this/1000

fun Double.milesToKm() = this * 1.6