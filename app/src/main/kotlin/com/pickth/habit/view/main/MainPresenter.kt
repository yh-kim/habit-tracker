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

package com.pickth.habit.view.main

import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import com.google.gson.Gson
import com.pickth.habit.base.mvp.BaseView
import com.pickth.habit.util.HabitManager
import com.pickth.habit.listener.OnHabitTouchListener
import com.pickth.habit.listener.OnHabitDragListener
import com.pickth.habit.util.StringUtil
import com.pickth.habit.view.main.adapter.item.Habit
import com.pickth.habit.view.main.adapter.MainAdapterContract
import com.pickth.habit.view.main.adapter.viewholder.HabitViewHolder

/**
 * Created by yonghoon on 2017-08-09
 */

class MainPresenter: MainContract.Presenter, OnHabitTouchListener, OnHabitDragListener {

    val TAG = javaClass.simpleName

    private lateinit var mView: MainContract.View
    private lateinit var mAdapterView: MainAdapterContract.View
    private lateinit var mAdapterModel: MainAdapterContract.Model
    private lateinit var mHabitTouchHelper: ItemTouchHelper

    override fun attachView(view: BaseView<*>) {
        mView = view as MainContract.View
    }

    override fun setAdapterView(view: MainAdapterContract.View) {
        mAdapterView = view
        mAdapterView.setOnHabitClickListener(this)
        mAdapterView.setOnHabitDragListener(this)
    }

    override fun setAdapterModel(model: MainAdapterContract.Model) {
        mAdapterModel = model
    }

    override fun setTouchHelper(habitTouchHelper: ItemTouchHelper) {
        mHabitTouchHelper = habitTouchHelper
    }

    override fun getItemCount(): Int = mAdapterModel.getItemCount()

    override fun addHabitItem(item: Habit) {
        mAdapterModel.addItem(item)
        HabitManager.addHabit(mView.getContext(), item)
    }

    override fun addHabitItems(list: ArrayList<Habit>) {
        mAdapterModel.addItems(list)
    }

    override fun clearHabitItems() {
        mAdapterModel.removeAllItems()
        HabitManager.removeAllHabit(mView.getContext())
    }

    override fun moveHabitItem(startPosition: Int, endPosition: Int) {
        mAdapterModel.swapItem(startPosition, endPosition)
        HabitManager.notifyDataSetChanged(mView.getContext(), mAdapterModel.getAllItems())
//        HabitManager.swapHabit(mView.getContext(), startPosition, endPosition)
    }

    override fun onItemCheck(position: Int) {
        if(mAdapterModel.getItem(position).days.isEmpty() || mAdapterModel.getItem(position).days[0] != StringUtil.getCurrentDay()) {
            Log.i(TAG, "isChecked")
            mAdapterModel.getItem(position).days.add(0, StringUtil.getCurrentDay())

            HabitManager.notifyDataSetChanged(mView.getContext())
            mView.updateWidget()
            mAdapterModel.notifyChanged(position)
        }
    }

    override fun onItemUnCheck(position: Int) {
        if(mAdapterModel.getItemCount() != 0 && mAdapterModel.getItem(position).days[0] == StringUtil.getCurrentDay()) {
            Log.i(TAG, "isUnChecked")
            mAdapterModel.getItem(position).days.removeAt(0)

            HabitManager.notifyDataSetChanged(mView.getContext())
            mView.updateWidget()
            mAdapterModel.notifyChanged(position)
        }
    }

    override fun onItemRemove(position: Int) {
        mAdapterModel.removeItem(position)
        HabitManager.removeHabit(mView.getContext(), position)
    }

    override fun onItemModify(position: Int, habit: Habit) {
        mView.showModifyHabitDialog(position, habit)
    }

    override fun changeItem(position: Int, habit: Habit) {
        mAdapterModel.changeItem(position, habit)
        HabitManager.notifyDataSetChanged(mView.getContext())
    }

    override fun refreshAllData() {
        Log.v(TAG, "refreshAllData")
        for(i in 0 until getItemCount() - 1) {
            mAdapterModel.notifyChanged(i)
        }

        mView.updateWidget()
    }

    fun getHabitsWithJson(): String = Gson()
            .toJson(
                    HabitManager.getHabits(mView.getContext())
            )
            .toString()

    override fun onStartDrag(holder: HabitViewHolder) {
        mHabitTouchHelper.startDrag(holder)
    }

    override fun onUpdateItems() {
        refreshAllData()
    }
}