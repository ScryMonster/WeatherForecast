package com.example.weatherforecast.utils.extensions

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.models.dto.WhatToDraw
import com.example.weatherforecast.utils.adapter.SwipeableAdapter


fun RecyclerView.addSwipeCallback() {


    val adapter = adapter as? SwipeableAdapter
    val background = ColorDrawable()

    val callback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int =
                adapter?.whatToDraw(viewHolder.adapterPosition)?.let {
                    super.getSwipeDirs(recyclerView, viewHolder)
                } ?: 0


            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter?.onSwipe(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                adapter?.whatToDraw(viewHolder.adapterPosition)?.let { whatToDraw ->

                    val iconToDraw = ContextCompat.getDrawable(context, whatToDraw?.icon ?: -1)

                    val finalDX = if (whatToDraw == WhatToDraw.Save) dX / 2 else dX

                    val itemView = viewHolder.itemView
                    val iconMargin = (itemView.height - (iconToDraw?.intrinsicHeight ?: 0)) / 4
                    val iconTop =
                        itemView.top + (itemView.height - (iconToDraw?.intrinsicHeight ?: 0)) / 2
                    val iconBottom = iconTop + (iconToDraw?.intrinsicHeight ?: 0)
                    if (finalDX < 0) {

                        val iconLeft = itemView.right + finalDX.toInt() + iconMargin
                        val iconRight =
                            itemView.right + finalDX.toInt() + iconMargin + (iconToDraw?.intrinsicWidth
                                ?: 0)

                        iconToDraw?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(
                            itemView.right + finalDX.toInt() - iconMargin,
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    } else {
                        iconToDraw?.setBounds(0, 0, 0, 0)
                        background.setBounds(0, 0, 0, 0)
                    }
                    background.color = ContextCompat.getColor(context, whatToDraw?.color ?: -1)
                    background.draw(c)
                    iconToDraw?.draw(c)



                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        finalDX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return adapter?.whatToDraw(viewHolder.adapterPosition)?.let { 0.75f } ?: 0f
            }
        }

    ItemTouchHelper(callback).attachToRecyclerView(this)


}