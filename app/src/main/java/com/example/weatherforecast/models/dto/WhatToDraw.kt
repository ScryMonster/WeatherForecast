package com.example.weatherforecast.models.dto

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.example.weatherforecast.R

sealed class WhatToDraw(@DrawableRes val icon:Int,@ColorRes val color:Int) {
    object Save : WhatToDraw(icon = R.drawable.ic_save_24dp,color = R.color.colorPrimaryDark)
    object Delete : WhatToDraw(icon = R.drawable.ic_delete_24dp,color = R.color.indian_red)
}