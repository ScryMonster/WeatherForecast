package com.example.weatherforecast.arch.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

class NavControllerNavigator : Navigator {


    private var navController:NavController? = null
    private var navigationAdditionalAction:()->Unit = {}

    fun setNavController(navController: NavController){
        this.navController = navController
    }

    override fun setAdditionalActionBeforeNavigation(additionalAction: () -> Unit) {
        this.navigationAdditionalAction = additionalAction
    }

    override fun navigateToId(destinationId: Int, args: Bundle?, options: NavOptions?) {
        navigationAdditionalAction()
        navController?.navigate(destinationId,args,options)
    }

    override fun navigateToDirection(direction: NavDirections, options: NavOptions?) {
        navigationAdditionalAction()
        navController?.navigate(direction,options)
    }

    override fun navigateBackUntilId(destinationId: Int, inclusive: Boolean) {
        navigationAdditionalAction()
        navController?.popBackStack(destinationId, inclusive)
    }

    override fun navigateBack() {
        navigationAdditionalAction()
        navController?.popBackStack()
    }

    override fun stopNavigating() {

    }

    override fun navigateToRoot() {
        navigationAdditionalAction()
        val startDestination = navController?.graph?.startDestination ?: -1
        navController?.popBackStack(startDestination,false)
    }

}