package com.example.weatherforecast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.database.convertors.WeatherListConvertor
import com.example.weatherforecast.database.dao.WeatherDao
import com.example.weatherforecast.models.dto.CityWeather

@Database(
    entities = [CityWeather::class],
    version = 1
)
@TypeConverters(WeatherListConvertor::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    companion object {
        private var INSTANCE: WeatherDatabase? = null
        private const val databaseName = "weather-database.db"


        fun getInstance(context: Context): WeatherDatabase? {
            if (INSTANCE == null) {
                INSTANCE = create(context)
            }
            return INSTANCE
        }

        private fun create(context: Context) =
            Room.databaseBuilder(context, WeatherDatabase::class.java, databaseName)
                .build()
    }
}