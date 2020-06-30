package com.example.weatherforecast.arch.repository

import com.example.weatherforecast.arch.api.WeatherForecastAPI
import com.example.weatherforecast.database.dao.WeatherDao
import com.example.weatherforecast.models.dto.CityWeather
import com.example.weatherforecast.models.dto.CurrentWeatherResponse
import com.example.weatherforecast.models.dto.WeatherResponse
import com.example.weatherforecast.utils.extensions.applySchedulers
import com.example.weatherforecast.utils.mappers.mapToUIModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import retrofit2.HttpException

class WeatherRepository(
    private val weatherForecastAPI: WeatherForecastAPI,
    private val weatherDao: WeatherDao
) : IWeatherRepository {

    override fun get7DaysForecastByCityName(name: String): Single<WeatherResponse> =
        weatherForecastAPI.get7DaysForecastByLocation(name,daysAmount = 7)
            .applySchedulers()

    override fun get7DaysForecastByLocation(lat: Double, lon: Double): Single<WeatherResponse> =
        weatherForecastAPI.get7DaysForecastByLocation(lat, lon,daysAmount = 7)
            .applySchedulers()

    override fun getCurrentWeatherByCityName(name: String): Single<CurrentWeatherResponse> =
        weatherForecastAPI.getCurrentWeatherByCityName(name)
            .applySchedulers()
            .doOnError {
                if (it is HttpException && it.code() == 404) {
                    throw Error("Wrong city name. Please ty again")
                } else throw it
            }

    override fun getCurrentWeatherByLocation(
        lat: Double,
        lon: Double
    ): Single<CurrentWeatherResponse> =
        weatherForecastAPI.getCurrentWeatherByLocation(lat, lon)
            .applySchedulers()

    override fun saveCityWeather(cityWeather: CityWeather): Completable =
        weatherDao.insert(cityWeather).applySchedulers()

    override fun deleteCityWeather(cityWeather: CityWeather): Completable =
        weatherDao.delete(cityWeather).applySchedulers()

    override fun getSavedCities(): Single<List<CityWeather>> = weatherDao.getAllSavedCityWeathers()

    override fun getSaveAndCurrentCity(lat: Double, lon: Double): Single<List<CityWeather>> =
        Single.zip(
            getCurrentWeatherByLocation(lat, lon),
            getSavedCities(),
            BiFunction<CurrentWeatherResponse, List<CityWeather>, List<CityWeather>> { currentCityResponse, savedCities ->
                arrayListOf(currentCityResponse.mapToUIModel(isCurrent = true)).apply {
                    addAll(savedCities)
                }.toList()
            }
        ).applySchedulers()


}