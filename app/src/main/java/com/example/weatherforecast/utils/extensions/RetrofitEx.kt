package com.example.weatherforecast.utils.extensions

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


infix fun Request.build(chain: Interceptor.Chain): Response = chain.proceed(this)