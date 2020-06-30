package com.example.weatherforecast.arch.api

import com.example.weatherforecast.models.dto.CurrentWeatherResponse
import com.example.weatherforecast.models.dto.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastAPI {

    @GET("/forecast/daily")
    fun get7DaysForecastByLocation(
        @Query("q") name: String,
        @Query("cnt") daysAmount: Int
    ): Single<WeatherResponse>

    @GET("/forecast/daily")
    fun get7DaysForecastByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") daysAmount: Int
    ): Single<WeatherResponse>

    @GET("/weather")
    fun getCurrentWeatherByCityName(@Query("q") name: String): Single<CurrentWeatherResponse>

    @GET("/weather")
    fun getCurrentWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Single<CurrentWeatherResponse>

}