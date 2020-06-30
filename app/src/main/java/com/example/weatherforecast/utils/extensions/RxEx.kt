package com.example.weatherforecast.utils.extensions

import com.example.weatherforecast.arch.base.BaseViewModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.applySchedulers() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Completable.applySchedulers() = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.customSubscriber(
    viewModel: BaseViewModel,
    onSuccess: (T) -> Unit,
    onFail: ((Throwable) -> Unit)? = null
) {
    subscribe(object : SingleObserver<T> {

        override fun onSuccess(response: T) {
            onSuccess.invoke(response)
        }

        override fun onSubscribe(subscription: Disposable) {
            viewModel.addSubscription(subscription)
        }

        override fun onError(e: Throwable) {
            viewModel.hideProgress()
            onFail?.invoke(e) ?: run {
                viewModel.showErrorMessage(message = e.localizedMessage)
            }
        }
    })
}


fun Completable.customSubscriber(
    viewModel: BaseViewModel,
    onSuccess: () -> Unit,
    onFail: ((Throwable) -> Unit)? = null
) {
    subscribe(object : CompletableObserver {
        override fun onComplete() {
            onSuccess.invoke()
        }

        override fun onSubscribe(subscription: Disposable) {
            viewModel.addSubscription(subscription)
        }

        override fun onError(e: Throwable) {
            viewModel.hideProgress()
            onFail?.invoke(e) ?: run {
                viewModel.showErrorMessage(message = e.localizedMessage)
            }
        }
    })
}