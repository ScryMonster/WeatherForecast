package com.example.weatherforecast.arch.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.example.weatherforecast.arch.navigation.NavControllerNavigator
import com.example.weatherforecast.arch.navigation.Navigator
import com.example.weatherforecast.models.dto.Message
import com.example.weatherforecast.utils.Mockable
import com.example.weatherforecast.utils.extensions.customSubscriber
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject

@Mockable
abstract class BaseViewModel : ViewModel(), KoinComponent {

    private val navigator: Navigator by inject()
    private val subscriptions = CompositeDisposable()

    private val _progressStateHolder = MutableLiveData<Boolean>()
    val progressStateHolder: LiveData<Boolean> = _progressStateHolder

    private val _messageHolder = MutableLiveData<Message?>()
    val messageHolder: LiveData<Message?> = _messageHolder

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    init {
        navigator.setAdditionalActionBeforeNavigation {
            onNavigate()
        }
    }

    fun addSubscription(subscription: Disposable) {
        subscriptions.add(subscription)
    }

    fun setNavController(navController: NavController) {
        (navigator as NavControllerNavigator).setNavController(navController)
    }

    private fun onNavigate() {

    }

    fun navigateToId(
        @IdRes destinationId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null
    ) {
        navigator.navigateToId(destinationId, args, navOptions)
    }

    fun navigateToDirection(direction: NavDirections, navOptions: NavOptions? = null) {
        navigator.navigateToDirection(direction, navOptions)
    }

    fun navigateBack(inclusive: Boolean = false) {
        navigator.navigateBack()
    }

    fun navigateBackTillId(@IdRes destinationId: Int, inclusive: Boolean = false) {
        navigator.navigateBackUntilId(destinationId, inclusive)
    }


    fun showProgress() {
        _progressStateHolder.value = true
    }

    fun hideProgress() {
        _progressStateHolder.value = false
    }


    protected fun <T> Single<T>.subscribeOnChanges(
        onSuccess: (T) -> Unit,
        onFail: ((Throwable) -> Unit)? = null
    ) {
        customSubscriber(this@BaseViewModel, onSuccess, onFail)
    }

    protected fun <T> Single<T>.subscribeOnChanges(onSuccess: (T) -> Unit) {
        customSubscriber(this@BaseViewModel, onSuccess)
    }


    protected fun Completable.subscribeOnChanges(onSuccess: () -> Unit) {
        customSubscriber(this@BaseViewModel, onSuccess)
    }

    protected fun Completable.subscribeOnChanges(
        onSuccess: () -> Unit,
        onFail: ((Throwable) -> Unit)? = null
    ) {
        customSubscriber(this@BaseViewModel, onSuccess, onFail)
    }


    fun showInfoMessage(
        message: String? = null,
        messageRes: Pair<Int,Array<String>?>? = null,
        duration: Int = INFO_MASSAGE_DURATION
    ) {
        _messageHolder.value = Message.InfoMessage(message, messageRes, duration)
    }

    fun showErrorMessage(
        message: String? = null,
        messageRes: Pair<Int,Array<String>?>? = null,
        duration: Int = ERROR_MASSAGE_DURATION
    ) {
        _messageHolder.value = Message.ErrorMessage(message, messageRes, duration)
    }

    fun showSuccessMessage(
        message: String? = null,
        messageRes: Pair<Int,Array<String>?>? = null,
        duration: Int = SUCCESS_MASSAGE_DURATION
    ) {
        _messageHolder.value = Message.SuccessMessage(message, messageRes, duration)
    }

    fun dismissMessage() {
        _messageHolder.value = null
    }


    companion object {
        private const val INFO_MASSAGE_DURATION = Snackbar.LENGTH_LONG
        private const val ERROR_MASSAGE_DURATION = Snackbar.LENGTH_SHORT
        private const val SUCCESS_MASSAGE_DURATION = Snackbar.LENGTH_SHORT
    }
}