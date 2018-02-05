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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pickth.habit.R
import com.pickth.habit.listener.OnHabitTouchListener
import com.pickth.habit.listener.OnHabitDragListener
import com.pickth.habit.view.main.adapter.item.Habit
import com.pickth.habit.view.main.adapter.viewholder.HabitViewHolder
import kotlin.collections.ArrayList

/**
 * Created by yonghoon on 2017-08-09
 */

class MainAdapter: RecyclerView.Adapter<HabitViewHolder>(), MainAdapterContract.View, MainAdapterContract.Model {
    private var mItems = ArrayList<Habit>()
    private lateinit var mListener: OnHabitTouchListener
    private lateinit var mDragListener: OnHabitDragListener

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HabitViewHolder {
        val itemView = LayoutInflater
                .from(parent?.context)
                .inflate(R.layout.item_habit_long, parent, false)
        return HabitViewHolder(itemView, mListener, mDragListener)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.onBind(mItems[position], position)
    }

    override fun getItemCount(): Int = mItems.size

    override fun setOnHabitClickListener(listener: OnHabitTouchListener) {
        mListener = listener
    }

    override fun setOnHabitDragListener(listener: OnHabitDragListener) {
        mDragListener = listener
    }

    override fun addItem(item: Habit) {
        mItems.add(itemCount, item)
        notifyItemInserted(itemCount)
    }

    override fun addItem(item: Habit, position: Int) {
        mItems.add(position, item)
        notifyItemInserted(position)
    }

    override fun addItems(list: ArrayList<Habit>) {
        for(i in list) addItem(i)
    }

    override fun getItem(position: Int): Habit = mItems[position]

    override fun getAllItems(): ArrayList<Habit> = mItems

    override fun removeItem(position: Int): Boolean {
        if(mItems.isEmpty()) return false

        mItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        return true
    }

    override fun removeAllItems() {
        mItems.clear()
        notifyDataSetChanged()
    }

    override fun changeItem(position: Int, habit: Habit) {
        mItems[position] = habit
        notifyChanged(position)
    }

    override fun swapItem(startPosition: Int, endPosition: Int) {
//        Collections.swap(mItems, startPosition, endPosition)
        val item = mItems[startPosition]
        mItems.remove(item)
        mItems.add(endPosition, item)
        notifyItemMoved(startPosition, endPosition)
    }

    override fun notifyChanged(position: Int) {
        notifyItemChanged(position)
    }

}