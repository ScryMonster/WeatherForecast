package com.example.weatherforecast.arch

import android.app.Application
import com.example.weatherforecast.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger

class WeatherForecast : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin(){
        startKoin {
            androidContext(applicationContext)
            logger(AndroidLogger())
            modules(BaseModule.module,NetModule.module,APIModule.module,RepositoryModule.module,ViewModelModule.module)
        }
    }
}