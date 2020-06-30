package com.example.weatherforecast.search

import com.example.weatherforecast.arch.ui.search.SearchViewModel
import com.example.weatherforecast.base.BaseKoinTest
import com.example.weatherforecast.models.dto.CurrentWeatherResponse
import com.example.weatherforecast.utils.mappers.mapToUIModel
import com.nhaarman.mockitokotlin2.any
import org.junit.Before
import org.junit.Test

class SearchViewModel_getCurrentWeatherByLocation_tests : BaseKoinTest<SearchViewModel>() {

    override lateinit var viewModel: SearchViewModel

    override fun SearchViewModel.startCall() {
        getCurrentWeatherByLocation(lat = mockedLat,lon = mockedLon)
    }

    @Before
    fun before(){
        viewModel = SearchViewModel(weatherRepository = weatherRepository)
    }

    @Test
    override fun successResponse() {
        weatherRepository.whenRepositoryCall {
            getCurrentWeatherByLocation(lat = mockedLat,lon = mockedLon)
        } answerWith {
            mockedCurrentCityResponse
        }

        spyViewModel {
            startCall()

            checkLoaderWasShown()
            checkErrorWasNotShown()
            currentCityWeatherHolder.checkValueIs(mockedMappedCurrentCityResponse)
        }
    }
    @Test
    override fun errorResponse() {
        weatherRepository.whenRepositoryCall {
            getCurrentWeatherByLocation(lat = mockedLat,lon = mockedLon)
        }.answerWithException()

        spyViewModel {
            startCall()

            checkLoaderWasShown()
            checkErrorWasShownWithMessage(mockedErrorMessage)
            searchedCityWeather.mockAndObserveForeverLiveData().checkSomeMethodWasCalled(0){
                onChanged(any())
            }
        }
    }


    companion object{
        private val mockedCityName = "Lviv"
        private val mockedLat = 20.88
        private val mockedLon = 20.88
        private val mockedCurrentCityResponse = CurrentWeatherResponse(
            coordinates = mockedCoordinates,
            currentWeather = mockedWeatherList,
            cityName = mockedCityName,
            date = mockedDate,
            weatherDetails = mockedWeatherDetails,
            windDetails = mockedWindDetails,
            visibility = mockedVisibility
        )

        private val mockedMappedCurrentCityResponse = mockedCurrentCityResponse.mapToUIModel(isCurrent = true)
    }
}