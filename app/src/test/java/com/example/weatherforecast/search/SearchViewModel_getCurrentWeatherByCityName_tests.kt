package com.example.weatherforecast.search

import com.example.weatherforecast.arch.ui.search.SearchViewModel
import com.example.weatherforecast.base.BaseKoinTest
import com.example.weatherforecast.models.dto.CurrentWeatherResponse
import com.example.weatherforecast.utils.mappers.mapToUIModel
import org.junit.Before
import org.junit.Test

class SearchViewModel_getCurrentWeatherByCityName_tests : BaseKoinTest<SearchViewModel>() {

    override lateinit var viewModel: SearchViewModel

    override fun SearchViewModel.startCall() {
        getSearchedWeatherOfTheCity(mockedCityName)
    }

    @Before
    fun before(){
        viewModel = SearchViewModel(weatherRepository = weatherRepository)
    }

    @Test
    override fun successResponse() {
        weatherRepository.whenRepositoryCall {
            getCurrentWeatherByCityName(mockedCityName)
        } answerWith {
            mockedCurrentCityResponse
        }

        spyViewModel {
            startCall()

            checkLoaderWasShown()
            checkErrorWasNotShown()
            searchedCityWeather.checkValueIs(mockedMappedCurrentCityResponse)
        }
    }
    @Test
    override fun errorResponse() {
        weatherRepository.whenRepositoryCall {
            getCurrentWeatherByCityName(mockedCityName)
        }.answerWithException()

        spyViewModel {
            startCall()

            checkLoaderWasShown()
            checkErrorWasShownWithMessage(mockedErrorMessage)
            searchedCityWeather.checkValueIs(null)
        }
    }


    companion object{
        private val mockedCityName = "Lviv"
        private val mockedCurrentCityResponse = CurrentWeatherResponse(
            coordinates = mockedCoordinates,
            currentWeather = mockedWeatherList,
            cityName = mockedCityName,
            date = mockedDate,
            weatherDetails = mockedWeatherDetails,
            windDetails = mockedWindDetails,
            visibility = mockedVisibility
        )

        private val mockedMappedCurrentCityResponse = mockedCurrentCityResponse.mapToUIModel()
    }
}