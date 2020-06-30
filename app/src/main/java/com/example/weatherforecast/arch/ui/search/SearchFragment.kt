package com.example.weatherforecast.arch.ui.search

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.weatherforecast.R
import com.example.weatherforecast.arch.base.BaseFragment
import com.example.weatherforecast.models.enums.SearchAdapterState
import com.example.weatherforecast.utils.adapter.CityWeatherAdapter
import com.example.weatherforecast.utils.decorators.VerticalOffsetDecoration
import com.example.weatherforecast.utils.extensions.addSwipeCallback
import com.example.weatherforecast.utils.extensions.hideKeyboard
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.search_fragment_layout.*
import java.util.concurrent.TimeUnit


class SearchFragment : BaseFragment<SearchViewModel>() {


    private val cityWeatherAdapter = CityWeatherAdapter()
        .setOnClickListener { cityWeather ->
            sv_new_city.hideKeyboard()
            viewModel.goToCityWeatherDetails(cityWeather)
        }
        .setOnSaveClickListener { cityWeather ->
            viewModel.saveCityWeather(cityWeather)
        }
        .setOnDeleteClickListener { cityWeather ->
            viewModel.deleteCityWeather(cityWeather)
        }

    override val layoutId: Int = R.layout.search_fragment_layout

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = activity?.getSystemService()
        mainActivity?.showToolbar()
        requestCitiesWithCurrentLocation()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListeners()
        observeViewModel()
    }

    private fun initUI() {
        rv_city_weather.apply {
            adapter = cityWeatherAdapter
            if (itemDecorationCount < 1) {
                addItemDecoration(VerticalOffsetDecoration(12))
            }
            addSwipeCallback()
        }
        sv_new_city.apply {
            isActivated = true
            isIconified = false
        }
    }



    private fun initListeners() {


        sv_new_city
            .queryTextChanges()
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { searchedText ->
                if (searchedText.isEmpty()){
                    cityWeatherAdapter.setSearchAdapterState(SearchAdapterState.IDLE)
                    requestCitiesWithCurrentLocation()
                }
                else {
                    cityWeatherAdapter.setSearchAdapterState(SearchAdapterState.SEARCH)
                    viewModel.getSearchedWeatherOfTheCity(searchedText.toString())
                }
            }
            .addSubscription()


        if (mainActivity?.checkLocationPermission() == true) {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME,
                MIN_DISTANCE,
                object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        location?.let {
                            viewModel.getCurrentWeatherByLocation(it.latitude,it.longitude)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                    override fun onProviderEnabled(provider: String?) {}

                    override fun onProviderDisabled(provider: String?) {}

                })
        }
    }


    private fun requestCitiesWithCurrentLocation(){
        if (mainActivity?.checkLocationPermission() == true) {
            locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let { lastKnownLocation ->
                viewModel.getSaveAndCurrentCity(lat = lastKnownLocation.latitude,lon = lastKnownLocation.longitude)
            } ?: viewModel.getSavedCities()
        } else viewModel.getSavedCities()
    }

    private fun observeViewModel() {
        viewModel.searchedCityWeather.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                cityWeatherAdapter.newSearchResponse(response)
            } ?: cityWeatherAdapter.clearToCurrent()
        })

        viewModel.currentCityWeatherHolder.observe(viewLifecycleOwner, Observer { response ->
            cityWeatherAdapter.addItem(response)
        })

        viewModel.savedItemHolder.observe(viewLifecycleOwner, Observer { savedCity ->
            cityWeatherAdapter.saveItem(savedCity)
        })

        viewModel.deletedItemHolder.observe(viewLifecycleOwner, Observer { deletedCity ->
            cityWeatherAdapter.removeItem(deletedCity)
        })

        viewModel.citiesWeatherHolder.observe(viewLifecycleOwner, Observer { allCities ->
            cityWeatherAdapter.addItems(allCities)
        })
    }

    companion object{
        private const val MIN_TIME = 900000L
        private const val MIN_DISTANCE = 1000F
    }
}