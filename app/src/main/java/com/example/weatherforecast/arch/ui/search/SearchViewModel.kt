package com.example.weatherforecast.arch.ui.search

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforecast.R
import com.example.weatherforecast.arch.base.BaseViewModel
import com.example.weatherforecast.arch.repository.IWeatherRepository
import com.example.weatherforecast.models.dto.CityWeather
import com.example.weatherforecast.utils.Mockable
import com.example.weatherforecast.utils.mappers.mapToForecast
import com.example.weatherforecast.utils.mappers.mapToUIModel

@Mockable
class SearchViewModel(private val weatherRepository: IWeatherRepository) : BaseViewModel() {


    private val _currentCityWeatherHolder = MutableLiveData<CityWeather>()
    val currentCityWeatherHolder: LiveData<CityWeather> = _currentCityWeatherHolder

    private val _searchedCityWeather = MutableLiveData<CityWeather?>()
    val searchedCityWeather: LiveData<CityWeather?> = _searchedCityWeather

    private val _savedItemHolder = MutableLiveData<CityWeather>()
    val savedItemHolder: LiveData<CityWeather> = _savedItemHolder

    private val _deletedItemHolder = MutableLiveData<CityWeather>()
    val deletedItemHolder: LiveData<CityWeather> = _deletedItemHolder

    private val _citiesWeatherHolder = MutableLiveData<List<CityWeather>>()
    val citiesWeatherHolder: LiveData<List<CityWeather>> = _citiesWeatherHolder

    fun getSearchedWeatherOfTheCity(name: String?) {
        showProgress()
        weatherRepository.getCurrentWeatherByCityName(name ?: "")
            .subscribeOnChanges({ response ->
                hideProgress()
                _searchedCityWeather.value = response.mapToUIModel()
            }, { error ->
                _searchedCityWeather.value = null
                showErrorMessage(message = error.localizedMessage)
            })
    }

    fun getCurrentWeatherByLocation(lat: Double, lon: Double) {
        showProgress()
        weatherRepository.getCurrentWeatherByLocation(lat, lon)
            .subscribeOnChanges { response ->
                hideProgress()
                _currentCityWeatherHolder.value = response.mapToUIModel(isCurrent = true)
            }
    }

    fun deleteCityWeather(cityWeather: CityWeather) {
        showProgress()
        weatherRepository.deleteCityWeather(cityWeather)
            .subscribeOnChanges {
                hideProgress()
                showInfoMessage(messageRes = Pair(R.string.message_city_was_deleted, arrayOf(cityWeather.cityName)))
                _deletedItemHolder.value = cityWeather
            }
    }

    fun saveCityWeather(cityWeather: CityWeather) {
        showProgress()
        weatherRepository.saveCityWeather(cityWeather)
            .subscribeOnChanges({
                hideProgress()
                _savedItemHolder.value = cityWeather
                showInfoMessage(messageRes = Pair(R.string.message_city_was_saved,arrayOf(cityWeather.cityName)))
            },{ error ->
                if (error is SQLiteConstraintException){
                    showErrorMessage(messageRes = Pair(R.string.message_city_has_been_already_saved,arrayOf(cityWeather.cityName)))
                } else showErrorMessage(message = error.localizedMessage)
            })
    }

    fun getSavedCities() {
        showProgress()
        weatherRepository.getSavedCities()
            .subscribeOnChanges { savedCities ->
                hideProgress()
                _citiesWeatherHolder.value = savedCities
            }
    }

    fun getSaveAndCurrentCity(lat: Double, lon: Double) {
        showProgress()
        weatherRepository.getSaveAndCurrentCity(lat, lon)
            .subscribeOnChanges {  allCities ->
                hideProgress()
                _citiesWeatherHolder.value = allCities
            }

    }

    fun goToCityWeatherDetails(cityWeather: CityWeather) {
        navigateToDirection(SearchFragmentDirections.openCityDetails(cityWeather.mapToForecast()))
    }
}