package com.example.weatherforecast.arch.ui.splash

import com.example.weatherforecast.arch.base.BaseViewModel

class SplashViewModel : BaseViewModel() {

    fun navigateToSearch(){
        navigateToDirection(SplashFragmentDirections.openSearchFragment())
    }
}