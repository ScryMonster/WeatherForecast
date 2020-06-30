package com.example.weatherforecast.arch.base

import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible

interface IProgressActivity {

    val progressGroup:Group

    fun showProgress(){
        progressGroup.isVisible = true
    }

    fun hideProgress(){
        progressGroup.isVisible = false
    }
}