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

package com.pickth.habit.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pickth.habit.R
import com.pickth.habit.listener.OnHabitTouchListener
import com.pickth.habit.view.main.adapter.item.Habit
import kotlinx.android.synthetic.main.dialog_import_habit.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * Created by yonghoon on 2017-09-20
 * Blog   : http://blog.pickth.com
 */

class ImportHabitDialog(context: Context, val listener: View.OnClickListener): Dialog(context, R.style.AppTheme_NoTitle_Translucent) {
    private var mHabits = ArrayList<Habit>()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ImportHabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_import_habit)

        mAdapter = ImportHabitAdapter().apply {
            setOnHabitClickListener(object: OnHabitTouchListener {
                override fun onItemModify(position: Int, habit: Habit) {
                }

                override fun onItemCheck(position: Int) {
                }

                override fun onItemUnCheck(position: Int) {
                }

                override fun onItemRemove(position: Int) {
                    mAdapter.removeItem(position)
                    mHabits = mAdapter.getItems()
                }
            })
        }

        mRecyclerView = rv_get_habits.apply {
            adapter = mAdapter
//            recycledViewPool.setMaxRecycledViews(0,0)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        iv_import_habit_back.setOnClickListener { onBackPressed() }

        btn_get_habits_info.setOnClickListener {
            val input = et_import_habits.text.toString().trim()

            if(input.length == 0) {
                context.toast(context.getString(R.string.input_habits_info))
                return@setOnClickListener
            }

            tv_get_habits_hint.visibility = View.GONE
            mAdapter.clear()

            try {
                JSONArray(input)

                val type = object: TypeToken<ArrayList<Habit>>(){}.type
                mHabits = Gson().fromJson<ArrayList<Habit>>(input, type)

                for(habit in mHabits) {
                    habit.id = UUID.randomUUID().toString()
                }

                mAdapter.addItems(mHabits)
                tv_get_habits_hint.visibility = View.VISIBLE

                // hide keyboard
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow((currentFocus ?: View(context)).windowToken, 0)
                window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                currentFocus?.clearFocus()
            } catch (e: JSONException) {
                context.toast(context.getString(R.string.incorrect_habit_info))
            }
        }

        iv_import_habit_check.setOnClickListener(listener)
    }

    fun getHabits(): ArrayList<Habit>? {
        if(mHabits.size == 0) {
            context.toast(context.getString(R.string.get_habits))
            return null
        }

        dismiss()
        return mHabits
    }
}