package com.example.weatherforecast.di

import android.app.Application
import com.example.weatherforecast.BuildConfig
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetModule {

    val module = module {
        single { provideCache(application = get()) }
        single { provideConverterFactory(gson = get()) }
        single { provideCallAdapterFactory() }
        single { provideGson() }
        single { provideOkHttpClient(cache = get()) }
        single {
            buildRetrofit(
                okHttpClient = get(),
                converter = get(),
                callAdapter = get()
            )
        }
    }


    private fun buildRetrofit(
        okHttpClient: OkHttpClient,
        converter: Converter.Factory,
        callAdapter: CallAdapter.Factory
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(converter)
        .addCallAdapterFactory(callAdapter)
        .build()

    private fun provideOkHttpClient(cache: Cache) = OkHttpClient.Builder().also { clientBuilder ->
        clientBuilder.addInterceptor { chain ->
            val request = chain.request().newBuilder().apply {
                addHeader("x-rapidapi-host", BuildConfig.HEADER_HOST)
                addHeader("x-rapidapi-key", BuildConfig.HEADER_KEY)
                addHeader("x-rapidapi-useQueryString", "true")
            }.build()
            chain.proceed(request)
        }

//        clientBuilder.addInterceptor { chain ->
//
//            val originRequest = chain.request()
//            val url = originRequest.url.newBuilder().apply {
//                addQueryParameter("appid", BuildConfig.WEATHER_API_KEY)
//            }.build()
//            val request = originRequest.newBuilder().url(url).build()
//            chain.proceed(request)
//        }
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(
                HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY }
            )
        }
        clientBuilder.cache(cache)
        clientBuilder.connectTimeout(timeoutSeconds.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds.toLong(), TimeUnit.SECONDS)

    }.build()


    private fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    private fun provideConverterFactory(gson: Gson): Converter.Factory =
        GsonConverterFactory.create(gson)

    private fun provideCallAdapterFactory(): CallAdapter.Factory =
        RxJava2CallAdapterFactory.create()

    private fun provideGson(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        .create()


    private const val timeoutSeconds = BuildConfig.TIMEOUT_SECONDS

}