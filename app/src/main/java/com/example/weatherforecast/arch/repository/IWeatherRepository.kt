package com.example.weatherforecast.arch.repository

import com.example.weatherforecast.models.dto.CityWeather
import com.example.weatherforecast.models.dto.CurrentWeatherResponse
import com.example.weatherforecast.models.dto.WeatherResponse
import io.reactivex.Completable
import io.reactivex.Single

interface IWeatherRepository {

    fun get7DaysForecastByCityName(name: String): Single<WeatherResponse>

    fun getCurrentWeatherByCityName(name: String): Single<CurrentWeatherResponse>

    fun getCurrentWeatherByLocation(
        lat: Double,
        lon: Double
    ): Single<CurrentWeatherResponse>

    fun get7DaysForecastByLocation(lat: Double, lon: Double): Single<WeatherResponse>

    fun saveCityWeather(cityWeather:CityWeather) : Completable

    fun deleteCityWeather(cityWeather: CityWeather) : Completable

    fun getSavedCities() : Single<List<CityWeather>>

    fun getSaveAndCurrentCity(lat: Double,lon: Double):Single<List<CityWeather>>

}