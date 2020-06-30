package com.example.weatherforecast.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.weatherforecast.arch.base.BaseViewModel
import com.example.weatherforecast.arch.navigation.NavControllerNavigator
import com.example.weatherforecast.arch.navigation.Navigator
import com.example.weatherforecast.arch.repository.IWeatherRepository
import com.example.weatherforecast.models.dto.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.KoinTestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.stubbing.OngoingStubbing
import java.net.SocketTimeoutException
import kotlin.random.Random


abstract class BaseKoinTest<VM : BaseViewModel> : AutoCloseKoinTest() {

    @Mock
    lateinit var weatherRepository: IWeatherRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mockitoRule = MockitoJUnit.rule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single<Navigator> { NavControllerNavigator() }
            }
        )
    }

    abstract val viewModel: VM

    @Test
    abstract fun successResponse()

    @Test
    abstract fun errorResponse()


    /**
     * This method designed to use only on repositories.
     * */
    protected fun <T, R> T.whenRepositoryCall(repositoryCall: T.() -> R) =
        Mockito.`when`(repositoryCall())


    /**
     * This method designed to use in pair with {@link whenRepositoryCall}
     **/
    protected infix fun <R> OngoingStubbing<R>.answerWith(answerCode: () -> Any) {
        thenAnswer { Single.just(answerCode()) }
    }

    /**
     * This method designed to use in pair with {@link whenRepositoryCall}. It trows an exception with mocked errorMessage {@see mockedErrorMessage}
     **/
    protected fun <R> OngoingStubbing<R>.answerWithException() =
        throwMockedError(mockedErrorMessage)


    /**
     * This method designed to use in pair with {@link whenRepositoryCall},which returns OngoingStubbing<Completable>.
     * It returns Completable.complete()
     **/
    protected fun OngoingStubbing<Completable>.answerWithComplete() =
        thenAnswer { Completable.complete() }

    /**
     * This method designed to use in pair with {@link whenRepositoryCall},which returns OngoingStubbing<Completable>.
     * It trows an exception with mocked errorMessage {@see mockedErrorMessage}
     **/
    protected fun OngoingStubbing<Completable>.answerWithExceptionCompletable() =
        thenAnswer { Completable.error(Throwable(mockedErrorMessage)) }


    abstract fun VM.startCall()

    protected fun spyViewModel(checkBlock: VM.() -> Unit) {
        with(createSpiedViewModel()) {
            checkBlock(this)
        }
    }

    private fun <T> OngoingStubbing<T>.throwMockedError(mockedErrorMessage: String = "") {
        thenAnswer {
            return@thenAnswer Single.error<SocketTimeoutException>(
                SocketTimeoutException(mockedErrorMessage)
            )
        }
    }

    protected fun VM.checkLoaderWasShown(times: Int = 1) {
        Mockito.verify(this, times(times)).showProgress()
        Mockito.verify(this, times(times)).hideProgress()
    }

    protected fun VM.checkLoaderWasNeverShown() {
        Mockito.verify(this, never()).showProgress()
        Mockito.verify(this, never()).hideProgress()
    }

    private fun createSpiedViewModel(): VM = Mockito.spy(viewModel)

    protected fun VM.checkErrorWasNotShown() {
        Mockito.verify(this, never()).showErrorMessage(messageRes = any(),message = anyString(),duration = anyInt())
    }

    protected fun VM.checkErrorWasShownWithMessage(
        message: String? = null,
        messageRes: Pair<Int, Array<String>?>? = null
    ) {
        message?.let {
            Mockito.verify(this, times(1)).showErrorMessage(message = it)
        } ?: run {
            Mockito.verify(this, times(1)).showErrorMessage(messageRes = messageRes)
        }
    }

    protected fun <R> R.checkSomeMethodWasCalled(times: Int = 0, methodCall: R.() -> Any) {
        Mockito.verify(this, if (times == 0) never() else times(times)).methodCall()
    }

    protected fun <T> LiveData<T>.mockAndObserveForeverLiveData(): Observer<T> {
        return (Mockito.mock(Observer::class.java) as Observer<T>).apply {
            observeForever(this)
        }
    }

    protected fun <T> LiveData<T>.checkValueIs(neededValue: T?) {
        Assert.assertEquals(value, neededValue)
    }

    companion object {
        const val mockedErrorMessage = "mockedErrorMessage"

        val mockedTemperature
            get() = Temperature(
                day = 300.0,
                min = 290.0,
                max = 320.0,
                night = 295.0,
                morning = 293.0,
                evening = 305.0
            )

        val mockedCoordinates = Coordinate(lat = 49.8397, lon = 24.0297)

        val mockedWeatherDay
            get() = WeatherDay(
                date = 1592818132822,
                sunriseTime = 1592818132822,
                sunsetTime = 1592818132822,
                temperature = mockedTemperature,
                pressure = 555.0,
                humidity = 10.0,
                windSpeed = 24.0,
                weather = listOf(
                    WeatherDescription(
                        id = Random.nextLong(),
                        main = "Clouds",
                        description = "Some clouds",
                        icon = "gg"
                    )
                )
            )

        val mockedWeather = Weather(
            id = Random.nextLong(),
            mainDescription = "Clouds",
            fullDescription = "Some clouds",
            icon = "gg"
        )

        val mockedWeatherList = listOf(mockedWeather)

        val mockedDate = Random.nextLong()

        val mockedWindDetails = WindDetails(
            speed = 4.0,
            degree = 0.0
        )

        val mockedWeatherDetails = WeatherDetails(
            temperature = 300.0,
            maxTemperature = 330.0,
            minTemperature = 280.0,
            pressure = 1014.0,
            humidity = 93.0
        )

        val mockedVisibility = 5000L
    }
}