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
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.formats.NativeContentAdView
import com.pickth.habit.R
import com.pickth.habit.listener.OnHabitTouchListener
import com.pickth.habit.listener.OnHabitDragListener
import com.pickth.habit.view.main.adapter.item.AdItem
import com.pickth.habit.view.main.adapter.item.Habit
import com.pickth.habit.view.main.adapter.item.PlusHabit
import com.pickth.habit.view.main.adapter.item.viewholder.AdViewHolder
import com.pickth.habit.view.main.adapter.item.viewholder.HabitViewHolder
import com.pickth.habit.view.main.adapter.item.viewholder.MainViewHolder
import kotlin.collections.ArrayList

/**
 * Created by yonghoon on 2017-08-09
 */

class MainAdapter: RecyclerView.Adapter<MainViewHolder>(), MainAdapterContract.View, MainAdapterContract.Model {

    companion object {
        val HABIT_TYPE_ITEM = 0
        val HABIT_TYPE_PLUS = 1
        val HABIT_TYPE_AD = 2
    }

    private var mIsUsedAd = false

    private var mItems = ArrayList<Habit>()
    private lateinit var mAdBuilder: AdLoader.Builder
    private lateinit var mListener: OnHabitTouchListener
    private lateinit var mDragListener: OnHabitDragListener

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder {
        if (viewType == HABIT_TYPE_AD) {
            val mAdView = LayoutInflater
                    .from(parent?.context)
                    .inflate(R.layout.ad_content, parent, false) as NativeContentAdView
            return AdViewHolder(mAdView, mAdBuilder)
        } else {

            val itemView = LayoutInflater
                    .from(parent?.context)
                    .inflate(R.layout.item_habit_long, parent, false)

            return HabitViewHolder(itemView, mListener, mDragListener)
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        if(getItemViewType(position) == HABIT_TYPE_AD) {
            holder.onBind()
        } else {
            holder.onBind(mItems[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(mItems[position] is PlusHabit) {
            // last item
            return HABIT_TYPE_PLUS
        } else if(mItems[position] is AdItem) {
            return HABIT_TYPE_AD
        } else {
            return HABIT_TYPE_ITEM
        }
    }

    override fun setAdBuilder(builder: AdLoader.Builder) {
        mAdBuilder = builder
        mIsUsedAd = true
    }

    override fun getItemCount(): Int = mItems.size

    override fun getHabitItemCount(): Int {
        var count = itemCount
        if(isExistPlus())
            count--

        if(mIsUsedAd)
            count--

        return count
    }

    override fun setOnHabitClickListener(listener: OnHabitTouchListener) {
        mListener = listener
    }

    override fun setOnHabitDragListener(listener: OnHabitDragListener) {
        mDragListener = listener
    }

    override fun addItem(item: Habit) {
        mItems.add(getHabitItemCount(), item)
//        notifyDataSetChanged()
        notifyItemInserted(getHabitItemCount())
    }

    override fun addItem(item: Habit, position: Int) {
        mItems.add(position, item)
//        notifyDataSetChanged()
        notifyItemInserted(position)
    }

    override fun addItems(list: ArrayList<Habit>) {
        for(i in list) addItem(i)
    }

    override fun getItem(position: Int): Habit = mItems[position]

    override fun getAllItems(): ArrayList<Habit> = mItems

    override fun getHabitItems(): ArrayList<Habit> {
        return (mItems.clone() as ArrayList<Habit>).apply {
            removeAt(itemCount-1) // ad
//            removeAt(itemCount-2) // plus
        }
    }

    override fun removeItem(position: Int): Boolean {
        if(mItems.isEmpty()) return false

        mItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        return true
    }

    override fun removeAllItems() {
        mItems.clear()

        addItem(PlusHabit(), itemCount)
        if(mIsUsedAd)
            addItem(AdItem(), itemCount)

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
        notifyItemChanged(endPosition)
    }

    override fun notifyChanged(position: Int) {
        notifyItemChanged(position)
    }

    override fun isExistPlus(): Boolean {
        if(mIsUsedAd) {
            if(itemCount > 1) {
                // 아이템이 있는 상황
                return mItems[itemCount-2] is PlusHabit
            } else {
                // all remove 하고 추가할 때, 0이면 false 1이면 확인
                return itemCount == 1 && mItems[itemCount-1] is PlusHabit
            }
        } else {
            return itemCount>0 && mItems[itemCount-1] is PlusHabit
        }
    }

    override fun getIsUsedAd(): Boolean = mIsUsedAd
}