package com.example.weatherforecast.weatherDetails

import com.example.weatherforecast.arch.ui.details.WeatherDetailsViewModel
import com.example.weatherforecast.base.BaseKoinTest
import com.example.weatherforecast.models.dto.City
import com.example.weatherforecast.models.dto.WeatherResponse
import com.nhaarman.mockitokotlin2.any
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class WeatherDetailsViewModel_get7DaysForecastByLocation_tests :
    BaseKoinTest<WeatherDetailsViewModel>() {
    override lateinit var viewModel: WeatherDetailsViewModel

    override fun WeatherDetailsViewModel.startCall() {
        getForecastForCurrentCity(lat = mockedLat, lon = mockedLon)
    }

    @Before
    fun before() {
        viewModel = WeatherDetailsViewModel(weatherRepository = weatherRepository)
    }

    @Test
    override fun successResponse() {
        weatherRepository.whenRepositoryCall {
            get7DaysForecastByLocation(lat = mockedLat, lon = mockedLon)
        } answerWith {
            mockedWeatherResponse
        }

        spyViewModel {
            startCall()

            checkLoaderWasShown()
            checkErrorWasNotShown()
            weatherDaysHolder.checkValueIs(mockedWeatherDaysForecast)
        }
    }

    @Test
    override fun errorResponse() {
        weatherRepository.whenRepositoryCall {
            get7DaysForecastByLocation(lat = mockedLat, lon = mockedLon)
        }.answerWithException()

        spyViewModel {
            startCall()

            checkLoaderWasShown()
            checkErrorWasShownWithMessage(mockedErrorMessage)
            weatherDaysHolder.mockAndObserveForeverLiveData().checkSomeMethodWasCalled(0){
                onChanged(any())
            }
        }
    }

    companion object {
        private val mockedCityName = "Lviv"
        private val mockedLat = 20.88
        private val mockedLon = 20.88

        private val mockedWeatherDaysForecast = listOf(
            mockedWeatherDay,
            mockedWeatherDay,
            mockedWeatherDay,
            mockedWeatherDay,
            mockedWeatherDay,
            mockedWeatherDay,
            mockedWeatherDay
        )

        private val mockedWeatherResponse
            get() = WeatherResponse(
                city = City(
                    id = Random.nextInt(),
                    name = mockedCityName,
                    coordinate = mockedCoordinates
                ),
                days = mockedWeatherDaysForecast
            )
    }
}