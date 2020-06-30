package com.example.weatherforecast.utils.adapter

import com.example.weatherforecast.models.dto.WhatToDraw

interface SwipeableAdapter {
    
    fun onSwipe(position:Int)

    fun whatToDraw(position: Int):WhatToDraw?
}