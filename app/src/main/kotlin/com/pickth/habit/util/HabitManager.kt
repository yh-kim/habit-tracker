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
import com.pickth.habit.view.main.adapter.item.Habit
import java.util.*

/**
 * Created by yonghoon on 2017-08-13
 */

object HabitManager {
    private var mHabits = ArrayList<Habit>()
    val TAG = "${javaClass.simpleName}"

    fun getHabits(context: Context): ArrayList<Habit> {
        val applicationContext = context.applicationContext
        if(mHabits.size == 0) {
            val json = getHabitInfoWithPref(applicationContext)
            if(json != "") {
                // items 를 파일에 저장하고 Preferences 에는 ""를 저장시킨다.
                val type = object: TypeToken<ArrayList<Habit>>(){}.type
                mHabits = Gson().fromJson<ArrayList<Habit>>(json, type)
                notifyDataSetChanged(applicationContext)
                setNullIntoPref(applicationContext)
                return mHabits
            }

            val habits = getHabitInfoWithFile()
            if(habits != null) {
                mHabits = habits
            }
        }

        return mHabits
    }

    private fun getHabitInfoWithPref(context: Context): String = context.applicationContext
            .getSharedPreferences("habits", Context.MODE_PRIVATE)
            .getString("habits", "")

    private fun getHabitInfoWithFile(): ArrayList<Habit>? {
        return null
    }

    private fun setHabitInfoWithFile(habits: ArrayList<Habit>) {

    }

    private fun setNullIntoPref(context: Context) {
        val applicationContext = context.applicationContext
        applicationContext.getSharedPreferences("habits", Context.MODE_PRIVATE)
                .edit()
                .putString("habits", "")
                .apply()
    }

    fun notifyDataSetChanged() {
        setHabitInfoWithFile(mHabits)
    }

    fun notifyDataSetChanged(context: Context) {
        setHabitInfoWithFile(mHabits)
    }

    fun notifyDataSetChanged(context: Context, habits: ArrayList<Habit>) {
        setHabitInfoWithFile(habits)
    }

    fun addHabit(context: Context, habit: Habit) {
        val applicationContext = context.applicationContext
        getHabits(applicationContext).add(habit)
        notifyDataSetChanged()
    }

    fun removeHabit(context: Context, position: Int) {
        val applicationContext = context.applicationContext
        getHabits(applicationContext).removeAt(position)
        notifyDataSetChanged()
    }

    fun swapHabit(context: Context, startPosition: Int, endPosition: Int) {
        val applicationContext = context.applicationContext
        Collections.swap(getHabits(applicationContext), startPosition, endPosition)
        notifyDataSetChanged()
    }

    fun removeAllHabit(context: Context) {
        val applicationContext = context.applicationContext
        getHabits(applicationContext).clear()
        notifyDataSetChanged()
//        context
//                .getSharedPreferences("habits", Context.MODE_PRIVATE)
//                .edit()
//                .clear()
//                .apply()
    }

    fun logHabitStatus(context: Context) {
        val applicationContext = context.applicationContext
        for(i in getHabits(applicationContext)) {
            Log.v(TAG, i.toString())
        }
    }
}