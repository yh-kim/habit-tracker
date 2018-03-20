package com.pickth.habit.listener

import com.pickth.habit.view.main.adapter.viewholder.HabitViewHolder

/**
 * Created by yonghoon on 2017-12-18
 */

interface OnHabitDragListener {
    fun onStartDrag(holder: HabitViewHolder)


    fun onUpdateItems()
}