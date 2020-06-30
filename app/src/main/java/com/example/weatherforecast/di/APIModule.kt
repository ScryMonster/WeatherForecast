package com.example.weatherforecast.di

import com.example.weatherforecast.arch.api.WeatherForecastAPI
import org.koin.dsl.module
import retrofit2.Retrofit

object APIModule {


    val module = module {
        single<WeatherForecastAPI> {
            createAPI(get(),WeatherForecastAPI::class.java)
        }
    }

    private fun <T> createAPI(retrofit: Retrofit,clazz: Class<T>) = retrofit.create(clazz)

}