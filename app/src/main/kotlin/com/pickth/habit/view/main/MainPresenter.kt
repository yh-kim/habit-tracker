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
import com.pickth.habit.listener.OnHabitClickListener
import com.pickth.habit.listener.OnHabitDragListener
import com.pickth.habit.listener.OnHabitMoveListener
import com.pickth.habit.util.StringUtil
import com.pickth.habit.view.main.adapter.item.Habit
import com.pickth.habit.view.main.adapter.MainAdapterContract
import com.pickth.habit.view.main.adapter.MainViewHolder

/**
 * Created by yonghoon on 2017-08-09
 */

class MainPresenter: MainContract.Presenter, OnHabitClickListener, OnHabitDragListener {

    val TAG = "${javaClass.simpleName}"

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

    override fun getItemCount(): Int = mAdapterModel.getItemCount() - 1

    override fun addHabitItem(item: Habit) {
        mAdapterModel.addItem(item)
        HabitManager.addHabit(mView.getContext(), item)
    }

    override fun addHabitItems(list: ArrayList<Habit>) {
        mAdapterModel.addItems(list)
    }

    override fun moveHabitItem(startPosition: Int, endPosition: Int) {
        // 마지막 아이템이면
        if(mAdapterModel.getItemCount() - 1 == endPosition) {
            return
        }

        mAdapterModel.swapItem(startPosition, endPosition)
        HabitManager.notifyDataSetChanged(mView.getContext())
    }

    override fun onItemCheck(position: Int) {
        mAdapterModel.notifyChanged(position)
        mAdapterModel.getItem(position).days.add(0, StringUtil.getCurrentDay())

        HabitManager.notifyDataSetChanged(mView.getContext())
        mView.updateWidget()
    }

    override fun onItemUnCheck(position: Int) {
        mAdapterModel.notifyChanged(position)

        if(mAdapterModel.getItem(position).days[0] == StringUtil.getCurrentDay()) {

        }

        mAdapterModel.getItem(position).days.removeAt(0)

        HabitManager.notifyDataSetChanged(mView.getContext())
        mView.updateWidget()
    }

    override fun onItemLongClick(position: Int) {
        mAdapterModel.removeItem(position)
        HabitManager.removeHabit(mView.getContext(), position)
    }

    override fun onItemModify(position: Int, habit: Habit) {
        mView.showModifyHabitDialog(position, habit)
    }

    override fun onLastItemClick() {
        mView.showAddHabitDialog()
//        mView.scrollToLastItem()
    }

    override fun changeItem(position: Int, habit: Habit) {
        mAdapterModel.changeItem(position, habit)
        HabitManager.notifyDataSetChanged(mView.getContext())
    }

    override fun refreshAllData() {
        Log.v(TAG, "refreshAllData")
        for(i in 0..getItemCount()-1) {
            mAdapterModel.notifyChanged(i)
        }

        mView.updateWidget()
    }

    fun getHabitsWithJson(): String = Gson()
            .toJson(
                    HabitManager.getHabits(mView.getContext())
            )
            .toString()

    override fun onStartDrag(holder: MainViewHolder) {
        mHabitTouchHelper.startDrag(holder)
    }
}