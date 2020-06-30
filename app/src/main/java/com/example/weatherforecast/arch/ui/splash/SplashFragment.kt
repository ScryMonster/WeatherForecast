package com.example.weatherforecast.arch.ui.splash

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.weatherforecast.R
import com.example.weatherforecast.arch.activity.MainActivity.Companion.LocationRequestCode
import com.example.weatherforecast.arch.base.BaseFragment
import kotlinx.android.synthetic.main.splash_fragment_layout.*

class SplashFragment : BaseFragment<SplashViewModel>() {

    override val layoutId: Int = R.layout.splash_fragment_layout

    private val isLocationActivated: Boolean
        get() = mainActivity?.checkLocationPermission() == true

    private var bounceAnimation: Animation? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity?.hideToolbar()
        initListeners()
        checkLocationPermission()
    }

    private fun initListeners() {}

    private fun checkLocationPermission() {
        if (isLocationActivated) startThreeRepeatsAnimation()
        else {
            startInfinitiveBounceAnimation()
            requestLocationPermission()
        }
    }

    private fun startThreeRepeatsAnimation() {
        val threeRepeatsAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.ic_sun_splash_anim_3_repeats)
        threeRepeatsAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                animation?.cancel()
                startFullScreenAnimation()
            }

            override fun onAnimationStart(animation: Animation?) {}

        })
        iv_splash_sun.startAnimation(threeRepeatsAnimation)
    }

    private fun startInfinitiveBounceAnimation() {
        bounceAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.ic_sun_splash_anim_infinitive)
        iv_splash_sun.startAnimation(bounceAnimation)
    }

    private fun startFullScreenAnimation() {
        val fullScreenAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.ic_sun_splash_anim_full_screen)
        fullScreenAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                viewModel.navigateToSearch()
            }

            override fun onAnimationStart(animation: Animation?) {}

        })
        iv_splash_sun.startAnimation(fullScreenAnimation)
    }


    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            LocationRequestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LocationRequestCode && grantResults.all { it == 0 }) {
            bounceAnimation?.cancel()
            startFullScreenAnimation()
        }
    }

}