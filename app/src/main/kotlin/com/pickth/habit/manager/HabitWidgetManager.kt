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

package com.pickth.habit.manager

import android.content.Context
import com.pickth.habit.manager.HabitManager

/**
 * Created by yonghoon on 2017-08-15
 */

object HabitWidgetManager {
    fun addWidget(context: Context, widgetId: Int, habitId: String) {
        val applicationContext = context.applicationContext
        applicationContext.getSharedPreferences("habitWidget", 0)
                .edit()
                .putString("$widgetId", habitId)
                .apply()
    }

    fun getHabitPosition(context: Context, widgetId: Int): Int? {
        val applicationContext = context.applicationContext
        val habitId = applicationContext
                .getSharedPreferences("habitWidget", 0)
                .getString("$widgetId", "")
        val habits = HabitManager.getHabits(applicationContext)

        for(i in 0..habits.size - 1) {
            if(habits[i].id == habitId) {
                return i
            }
        }

        return null
    }

    fun removeWidget(context: Context, widgetId: Int) {

    }
}