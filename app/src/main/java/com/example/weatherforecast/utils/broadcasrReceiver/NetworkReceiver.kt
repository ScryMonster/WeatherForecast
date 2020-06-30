package com.example.weatherforecast.utils.broadcasrReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.NetworkRequest

class NetworkReceiver : BroadcastReceiver() {

    private val onNetworkAvailable:()->Unit = {}
    private val onNetworkNotAvailable:()->Unit = {}


    fun setOnNetworkAvailableListener(listener:()->Unit) = apply{

    }

    fun setOnNetworkNotAvailableListener(listener:()->Unit) = apply{

    }

    override fun onReceive(context: Context?, intent: Intent?) {
//        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo: NetworkInfo? = conn.activeNetworkInfo
//        conn.registerNetworkCallback(NetworkRequest.Builder().build(),object :ConnectivityManager.NetworkCallback(){
//            ove
//        })
    }
}