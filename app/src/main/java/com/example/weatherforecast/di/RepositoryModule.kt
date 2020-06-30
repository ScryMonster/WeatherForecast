package com.example.weatherforecast.di

import com.example.weatherforecast.arch.repository.IWeatherRepository
import com.example.weatherforecast.arch.repository.MockedWeatherRepository
import com.example.weatherforecast.arch.repository.WeatherRepository
import org.koin.dsl.module

object RepositoryModule {
    val module = module {
        single<IWeatherRepository> { MockedWeatherRepository(weatherDao = get()) }
    }
}