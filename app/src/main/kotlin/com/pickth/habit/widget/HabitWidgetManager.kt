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

package com.pickth.habit.widget

import android.content.Context
import android.util.Log
import com.pickth.habit.util.HabitManager

/**
 * Created by yonghoon on 2017-08-15
 */

object HabitWidgetManager {
    fun addWidget(context: Context, widgetId: Int, habitId: String) {
        context.getSharedPreferences("habitWidget", 0)
                .edit()
                .putString("$widgetId", habitId)
                .commit()
        Log.v("habit000", "add widget id : ${widgetId}")
    }

    fun getHabitPosition(context: Context, widgetId: Int): Int {
        val habitId = context
                .getSharedPreferences("habitWidget", 0)
                .getString("$widgetId", "")
        var habits = HabitManager.getHabits(context)
        for(i in 0..habits.size - 1) {
            if(habits[i].id == habitId) {
                Log.v("habit000", "get habit position : ${i}")
                return i
            }
        }

        return 0
    }

    fun removeWidget(context: Context, widgetId: Int) {

    }
}