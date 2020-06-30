package com.example.weatherforecast.models.dto

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.weatherforecast.R


sealed class Message(
    open val message: String?,
    open val messageRes: Pair<Int,Array<String>?>? = null,
    open val duration: Int,
    @ColorRes open val backgroundColor: Int,
    @ColorRes val textColor: Int = android.R.color.black
) {
    class InfoMessage(
        override val message: String?,
        override val messageRes: Pair<Int,Array<String>?>? = null,
        override val duration: Int
    ) : Message(message, messageRes, duration, R.color.windows_blue)

    class ErrorMessage(
        override val message: String?,
        override val messageRes: Pair<Int,Array<String>?>? = null,
        override val duration: Int
    ) : Message(message, messageRes, duration, R.color.indian_red)

    class SuccessMessage(
        override val message: String?,
        override val messageRes: Pair<Int,Array<String>?>? = null,
        override val duration: Int
    ) : Message(message, messageRes, duration, R.color.soft_green)
}




