package com.example.weatherforecast.di

import com.example.weatherforecast.arch.activity.MainActivityViewModel
import com.example.weatherforecast.arch.ui.details.WeatherDetailsViewModel
import com.example.weatherforecast.arch.ui.search.SearchViewModel
import com.example.weatherforecast.arch.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val module = module {
        viewModel { MainActivityViewModel() }
        viewModel { SplashViewModel() }
        viewModel { SearchViewModel(weatherRepository = get()) }
        viewModel { WeatherDetailsViewModel(weatherRepository = get()) }
    }
}