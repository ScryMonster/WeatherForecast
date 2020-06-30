package com.example.weatherforecast.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.weatherforecast.models.dto.CityWeather
import io.reactivex.Single

@Dao
interface WeatherDao  : BaseDao<CityWeather>{

    @Query("Select * from CityWeatherTable")
    fun getAllSavedCityWeathers() : Single<List<CityWeather>>
}