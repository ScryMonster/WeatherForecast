package com.example.weatherforecast.arch.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.arch.base.BaseViewModel
import com.example.weatherforecast.arch.repository.IWeatherRepository
import com.example.weatherforecast.models.dto.WeatherDay

class WeatherDetailsViewModel(private val weatherRepository:IWeatherRepository) : BaseViewModel() {

    private val _weatherDaysHolder = MutableLiveData<List<WeatherDay>>()
    val weatherDaysHolder: LiveData<List<WeatherDay>> = _weatherDaysHolder

    fun getForecastForCurrentCity(lat:Double,lon:Double){
        showProgress()
        weatherRepository.get7DaysForecastByLocation(lat,lon)
            .subscribeOnChanges {  response ->
                hideProgress()
                _weatherDaysHolder.value = response.days.take(7)
            }
    }
}