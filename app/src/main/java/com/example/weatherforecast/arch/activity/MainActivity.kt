package com.example.weatherforecast.arch.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.arch.base.ConnectivityRegister
import com.example.weatherforecast.arch.base.IProgressActivity
import com.example.weatherforecast.models.dto.Message
import com.example.weatherforecast.utils.extensions.showSnackBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), IProgressActivity {


    private val viewModel: MainActivityViewModel by viewModel()

    private val connectivityRegister: ConnectivityRegister by inject()

    override val progressGroup: Group
        get() = progress_group

    private val navController: NavController?
        get() = (nav_host_fragment_container as? NavHostFragment)?.navController

    private val isSplashFragment: Boolean
        get() = navController?.currentDestination?.id == R.id.splashFragment

    private var snackBar: Snackbar? = null

    private var isNetworkAvailable: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initListeners()
        observeViewModel()
        navController?.let { nav ->
            viewModel.setNavController(nav)

            main_toolbar.setupWithNavController(nav, AppBarConfiguration(nav.graph))

            main_toolbar.setNavigationOnClickListener {
                manageBackNavigation(nav)
            }
        }

        connectivityRegister
            .setConnectivityInitializer {
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            }
            .setOnConnectionAvailableListener {
                snackBar?.dismiss()
            }
            .setOnLostConnectionListener {
                runOnUiThread {
                    notifyAboutNetworkLost()
                }
            }

        lifecycle.addObserver(connectivityRegister)
        checkCurrentConnection()
    }

    private fun checkCurrentConnection(){
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.getNetworkCapabilities(cm.activeNetwork) ?: run {
            notifyAboutNetworkLost()
        }
    }

    private fun notifyAboutNetworkLost(){
        if (!isSplashFragment) {
            viewModel.showErrorMessage(
                messageRes = Pair(R.string.network_not_vailable, null),
                duration = Snackbar.LENGTH_INDEFINITE
            )
        } else isNetworkAvailable = false
    }

    private fun initListeners() {
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            if (controller.currentDestination?.id == R.id.splashFragment) {
                hideSystemUIAndNavigation(this)
            } else {
                showSystemUIAndNavigation(this)
                manageNetworkSnackBarBehaviour()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.messageHolder.observe(this, Observer { message ->
            message?.let {
                showMessage(message)
                viewModel.dismissMessage()
            }
        })
    }

    private fun manageNetworkSnackBarBehaviour() {
        if (!isNetworkAvailable) {
            snackBar?.let { bar ->
                if (!bar.isShown) {
                    showNetworkError()
                }
            } ?: showNetworkError()
        }
    }

    private fun showNetworkError() {
        viewModel.showErrorMessage(
            messageRes = Pair(R.string.network_not_vailable, null),
            duration = Snackbar.LENGTH_INDEFINITE
        )
    }

    //region show/hide toolbar logic
    fun hideToolbar() {
        main_toolbar.isVisible = false
    }

    fun showToolbar() {
        main_toolbar.isVisible = true
    }
    //endregion

    fun checkLocationPermission() =
        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun manageBackNavigation(navController: NavController) {
        when (navController.currentDestination?.id) {
            R.id.searchFragment -> finish()
            else -> navController.navigateUp()
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (isSplashFragment) {
            theme.applyStyle(R.style.SplashTheme, true)
        } else {
            theme.applyStyle(R.style.AppTheme, true)
        }
        return theme
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    private fun showSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun onBackPressed() {
        if (navController?.currentDestination?.id == R.id.searchFragment) finish()
        else super.onBackPressed()
    }

    private fun showMessage(message: Message) {

        fun formatStringFromResources(pair: Pair<Int, Array<String>?>): String {
            return pair.second?.let {
                getString(pair.first, *it)
            } ?: getString(pair.first)
        }

        snackBar = main_activity_container.showSnackBar(
            message.message ?: formatStringFromResources(message.messageRes ?: emptyMessageResPair),
            duration = message.duration,
            backgroundColor = message.backgroundColor,
            textColor = message.textColor
        )
    }


    companion object {
        const val LocationRequestCode = 10987
        private val emptyMessageResPair = Pair(-1, arrayOf<String>())
    }
}
