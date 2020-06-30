package com.example.weatherforecast.utils.mappers

import com.example.weatherforecast.models.dto.CityWeather
import com.example.weatherforecast.models.dto.CurrentWeatherResponse
import com.example.weatherforecast.models.dto.Forecast


fun CurrentWeatherResponse.mapToUIModel(isCurrent: Boolean = false) = CityWeather(
    coordinates = coordinates,
    currentWeather = currentWeather,
    cityName = cityName,
    date = date,
    weatherDetails = weatherDetails,
    isCurrent = isCurrent,
    windDetails = windDetails,
    visibility = visibility
)

fun CityWeather.mapToForecast() = Forecast(coordinates,cityName, currentWeather, weatherDetails,windDetails, visibility)