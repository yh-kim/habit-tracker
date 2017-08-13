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

package com.pickth.habit.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pickth.habit.view.main.adapter.Habit

/**
 * Created by yonghoon on 2017-08-13
 */

object HabitManagement {
    private var mHabits = ArrayList<Habit>()

    fun getHabits(context: Context): ArrayList<Habit> {
        if(mHabits.size == 0) {
            val json = context
                    .getSharedPreferences("habits", 0)
                    .getString("habits", "")

            if(json == "") return mHabits

            val type = object: TypeToken<ArrayList<Habit>>(){}.type
            mHabits = Gson().fromJson<ArrayList<Habit>>(json, type)
        }

        return mHabits
    }

    private fun notifyDataSetChanged(context: Context, habits: ArrayList<Habit>) {
        context.getSharedPreferences("habits", 0)
                .edit()
                .putString(Gson().toJson(habits).toString(), "habits")
                .commit()
    }

    fun addHabit(context: Context, habit: Habit) {
        mHabits.add(habit)
        notifyDataSetChanged(context, mHabits)
    }

    fun removeHabit(context: Context, position: Int) {
        mHabits.removeAt(position)
        notifyDataSetChanged(context, mHabits)
    }

    fun logHabitStatus(context: Context) {
        for(i in mHabits) {
            Log.v("habit", i.toString())
        }
    }
}