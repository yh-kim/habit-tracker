package com.pickth.habit.listener

import com.pickth.habit.view.main.adapter.MainViewHolder

/**
 * Created by yonghoon on 2017-12-18
 */

interface OnHabitDragListener {
    fun onStartDrag(holder: MainViewHolder)


    fun onUpdateItems()
}