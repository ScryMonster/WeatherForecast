package com.example.weatherforecast.arch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.weatherforecast.arch.activity.MainActivity
import com.example.weatherforecast.models.dto.Message
import com.example.weatherforecast.utils.extensions.inflate
import com.example.weatherforecast.utils.extensions.showSnackBar
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseFragment<ViewModel : BaseViewModel> : Fragment() {


    private val subscriptions = CompositeDisposable()

    abstract val layoutId: Int
        @IdRes get

    protected val viewModel: ViewModel by viewModel(clazz)

    private val clazz: KClass<ViewModel>
        get() = ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<ViewModel>).kotlin

    protected val mainActivity: MainActivity?
        get() = activity as? MainActivity


    protected val progressActivity: IProgressActivity?
        get() = mainActivity as? IProgressActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(layoutId)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.progressStateHolder.observe(viewLifecycleOwner, Observer { show ->
            if (show) showProgress()
            else hideProgress()
        })
    }

    private fun showProgress() {
        progressActivity?.showProgress()
        switchOffUiInteraction(true)
    }

    private fun hideProgress() {
        progressActivity?.hideProgress()
        switchOffUiInteraction(false)
    }


    private fun switchOffUiInteraction(flag: Boolean) {

        fun blockScreen() {
            mainActivity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }

        fun unBlockScreen() {
            mainActivity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        if (flag) blockScreen()
        else unBlockScreen()
    }


    protected fun Disposable.addSubscription() {
        subscriptions.add(this)
    }
}