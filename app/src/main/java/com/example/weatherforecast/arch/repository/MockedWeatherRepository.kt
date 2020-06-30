package com.example.weatherforecast.arch.repository

import com.example.weatherforecast.database.dao.WeatherDao
import com.example.weatherforecast.models.dto.*
import com.example.weatherforecast.utils.extensions.applySchedulers
import com.example.weatherforecast.utils.mappers.mapToUIModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlin.random.Random

class MockedWeatherRepository(
    private val weatherDao: WeatherDao
) : IWeatherRepository {

    override fun get7DaysForecastByCityName(name: String): Single<WeatherResponse> =
        Single.just(mockedWeatherResponse)

    override fun get7DaysForecastByLocation(lat: Double, lon: Double): Single<WeatherResponse> =
        Single.just(mockedWeatherResponse)

    override fun getCurrentWeatherByCityName(name: String): Single<CurrentWeatherResponse> =
        Single.just(mockedCurrentWeatherResponse)

    override fun getCurrentWeatherByLocation(
        lat: Double,
        lon: Double
    ): Single<CurrentWeatherResponse> =
        Single.just(mockedCurrentWeatherResponse)

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

    companion object {

        private val mockedCityName
            get() = "Lviv" + Random.nextInt()


        private val mockedTemperature
            get() = Temperature(
                day = 300.0,
                min = 290.0,
                max = 320.0,
                night = 295.0,
                morning = 293.0,
                evening = 305.0
            )

        private val mockedCoordinates = Coordinate(lat = 49.8397, lon = 24.0297)

        private val mockedWeatherDay
            get() = WeatherDay(
                date = 1592818132822,
                sunriseTime = 1592818132822,
                sunsetTime = 1592818132822,
                temperature = mockedTemperature,
                pressure = 555.0,
                humidity = 10.0,
                windSpeed = 24.0,
                weather = listOf(
                    WeatherDescription(
                        id = Random.nextLong(),
                        main = "Clouds",
                        description = "Some clouds",
                        icon = "gg"
                    )
                )
            )

        private val mockedWeatherResponse
            get() = WeatherResponse(
                city = City(
                    id = Random.nextInt(),
                    name = mockedCityName,
                    coordinate = mockedCoordinates
                ),
                days = listOf(
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay,
                    mockedWeatherDay
                )
            )

        private val mockedWeather = Weather(
            id = Random.nextLong(),
            mainDescription = "Clouds",
            fullDescription = "Some clouds",
            icon = "gg"
        )

        private val mockedWindDetails = WindDetails(
            speed = 4.0,
            degree = 0.0
        )

        private val mockedCurrentWeatherResponse
            get() = CurrentWeatherResponse(
                coordinates = mockedCoordinates,
                currentWeather = listOf(mockedWeather),
                cityName = mockedCityName,
                date = 1592818132822,
                weatherDetails = WeatherDetails(
                    temperature = 300.0,
                    maxTemperature = 330.0,
                    minTemperature = 280.0,
                    pressure = 1014.0,
                    humidity = 93.0
                ),
                windDetails = mockedWindDetails,
                visibility = 10000
            )

    }


}