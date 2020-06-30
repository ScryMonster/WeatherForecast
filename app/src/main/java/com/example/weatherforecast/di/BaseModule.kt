package com.example.weatherforecast.di

import androidx.room.Room
import com.example.weatherforecast.arch.base.ConnectivityRegister
import com.example.weatherforecast.arch.navigation.NavControllerNavigator
import com.example.weatherforecast.arch.navigation.Navigator
import com.example.weatherforecast.database.WeatherDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object BaseModule {
    val module = module {
        single<Navigator> { NavControllerNavigator() }
        single { WeatherDatabase.getInstance(androidApplication())?.getWeatherDao()!! }
        single { ConnectivityRegister() }
    }
}