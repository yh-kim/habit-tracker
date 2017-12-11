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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pickth.habit.R
import com.pickth.habit.view.main.adapter.item.Habit
import kotlinx.android.synthetic.main.item_habit_config.view.*
import org.jetbrains.anko.backgroundColor

/**
 * Created by yonghoon on 2017-08-15
 */

class HabitConfigAdapter: RecyclerView.Adapter<HabitConfigAdapter.HabitConfigViewHolder>() {
    private var mItems = ArrayList<Habit>()
    private lateinit var mListener: OnHabitConfigClickListener
    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: HabitConfigViewHolder?, position: Int) {
        holder?.onBInd(mItems[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HabitConfigViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_habit_config, parent, false)
        return HabitConfigViewHolder(view, mListener)
    }

    fun setOnClickListener(listener: OnHabitConfigClickListener) {
        mListener = listener
    }

    fun addItem(item: Habit) {
        mItems.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun getItem(position: Int): Habit = mItems[position]

    class HabitConfigViewHolder(view: View, val listener: OnHabitConfigClickListener): RecyclerView.ViewHolder(view) {
        fun onBInd(item: Habit, position: Int) {
            with(itemView) {
                ll_item_habit_config_back.backgroundColor = item.color
                tv_item_habit_config_title.text = item.title
            }

            itemView.setOnClickListener {
                listener.onClick(position)
            }
        }
    }

    interface OnHabitConfigClickListener {
        fun onClick(position: Int)
    }
}