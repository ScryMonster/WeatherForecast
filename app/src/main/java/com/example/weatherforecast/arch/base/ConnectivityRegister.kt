package com.example.weatherforecast.arch.base

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class ConnectivityRegister : LifecycleObserver {

    private var connectivityManager: ConnectivityManager? = null

    private var connectivityManagerInitializer: () -> ConnectivityManager = {
        throw Exception("You must set Connectivity Initializer in the activity by passing the lambda into the setConnectivityInitializer(...) function")
    }

    private var onLostConnectionListener: () -> Unit = {}

    private var onConnectionAvailableListener: () -> Unit = {}

    fun setOnLostConnectionListener(listener: () -> Unit) = apply {
        onLostConnectionListener = listener
    }

    fun setOnConnectionAvailableListener(listener: () -> Unit) = apply {
        onConnectionAvailableListener = listener
    }

    fun setConnectivityInitializer(initializer: () -> ConnectivityManager) = apply {
        connectivityManagerInitializer = initializer
    }

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerConnectivityManager() {
        connectivityManager = connectivityManagerInitializer()
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                onLostConnectionListener()
            }

            override fun onAvailable(network: Network) {
                onConnectionAvailableListener()
            }
        }

        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unRegisterConnectivityManager() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }
}