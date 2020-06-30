package com.example.weatherforecast.arch.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

interface Navigator {

    fun setAdditionalActionBeforeNavigation(additionalAction: () -> Unit)

    fun navigateToId(@IdRes destinationId:Int,args: Bundle? = null, options: NavOptions? = null)

    fun navigateToDirection(direction:NavDirections, options: NavOptions? = null)

    fun navigateBackUntilId(@IdRes destinationId:Int, inclusive:Boolean = false)

    fun navigateBack()

    fun stopNavigating()

    fun navigateToRoot()
}