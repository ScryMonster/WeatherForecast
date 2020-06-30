package com.example.weatherforecast.arch.ui.details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.weatherforecast.R
import com.example.weatherforecast.arch.base.BaseFragment
import com.example.weatherforecast.models.dto.Forecast
import com.example.weatherforecast.utils.adapter.WeatherDaysAdapter
import com.example.weatherforecast.utils.decorators.HorizontalOffsetDecoration
import com.example.weatherforecast.utils.kelvinToCelsius
import com.example.weatherforecast.utils.metresToKm
import com.example.weatherforecast.utils.milesToKm
import com.example.weatherforecast.utils.round
import kotlinx.android.synthetic.main.weather_details_fragment_layout.*


class WeatherDetailsFragment : BaseFragment<WeatherDetailsViewModel>() {

    private val weatherDaysAdapter = WeatherDaysAdapter()

    override val layoutId: Int = R.layout.weather_details_fragment_layout

    private val forecast: Forecast?
        get() = arguments?.getParcelable("forecast") as? Forecast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
        observeViewModel()
        mainActivity?.hideToolbar()
    }

    private fun iniUI() {
        rv_weather_details_days.apply {
            adapter = weatherDaysAdapter
            if (itemDecorationCount < 1) addItemDecoration(HorizontalOffsetDecoration(12))
        }

        forecast?.let { forecast ->

            tv_weather_details_city_name.text = forecast.cityName
            tv_weather_details_description.text = forecast.currentWeather[0].mainDescription
            tv_weather_details_current_temperature.text =
                getString(R.string.celcium_sign,forecast.weatherDetails.temperature.kelvinToCelsius().round())
            tv_weather_details_min_max_temperature.text = getString(
                R.string.min_max_temp,
                forecast.weatherDetails.minTemperature.kelvinToCelsius().round(),
                forecast.weatherDetails.maxTemperature.kelvinToCelsius().round()
            )

            wd_weather_details_wind_section.setValue(getString(R.string.km_hour_count, forecast.windDetails.speed.milesToKm().toString()))
            wd_weather_details_humidity_section.setValue(forecast.weatherDetails.humidity.toString() + "%")
            wd_weather_details_pressure_section.setValue(getString(R.string.pressure_count, forecast.weatherDetails.pressure.toInt().toString()))
            wd_weather_details_visibility_section.setValue(getString(R.string.km_count, forecast.visibility.metresToKm().toString()))

            viewModel.getForecastForCurrentCity(forecast.coordinate.lat, forecast.coordinate.lon)
        }
    }

    private fun observeViewModel() {
        viewModel.weatherDaysHolder.observe(viewLifecycleOwner, Observer { days ->
            days?.let {
                weatherDaysAdapter.addItems(it)
            }
        })
    }
}