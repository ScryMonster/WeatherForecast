package com.example.weatherforecast.utils.customView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weatherforecast.R
import kotlinx.android.synthetic.main.weather_description_view.view.*

class WeatherDescriptionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttributeSet: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttributeSet, defStyleRes) {

    init {
        initView(attrs, defStyleRes)
    }


    private fun initView(attrs: AttributeSet?, defStyleRes: Int?){
        View.inflate(context, R.layout.weather_description_view,this)
        val array = context.obtainStyledAttributes(attrs,R.styleable.WeatherDescriptionView,defStyleRes!!,0)
        val label = array.getString(R.styleable.WeatherDescriptionView_wd_label)
        tv_weather_description_view_label.text = label
    }

    fun setValue(value:String){
        tv_weather_description_view_value.text = value
    }
}