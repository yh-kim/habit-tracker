/*
 * Copyright 2017 Yonghoon Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pickth.habit.view.main.adapter

import com.pickth.habit.listener.OnHabitTouchListener
import com.pickth.habit.listener.OnHabitDragListener
import com.pickth.habit.view.main.adapter.item.Habit

/**
 * Created by yonghoon on 2017-08-09
 */

interface MainAdapterContract {
    interface View {
        fun setOnHabitClickListener(listener: OnHabitTouchListener)
        fun setOnHabitDragListener(listener: OnHabitDragListener)
    }

    interface Model {
        fun getItemCount(): Int
        fun addItem(item: Habit)
        fun addItem(item: Habit, position: Int)
        fun addItems(list: ArrayList<Habit>)
        fun getItem(position: Int): Habit
        fun getAllItems(): ArrayList<Habit>
        fun removeItem(position: Int): Boolean
        fun removeAllItems()
        fun changeItem(position: Int, habit: Habit)
        fun swapItem(startPosition: Int, endPosition: Int)
        fun notifyChanged(position: Int)
    }
}