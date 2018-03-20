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

import android.content.Context
import android.support.v7.widget.helper.ItemTouchHelper
import com.pickth.habit.base.mvp.BasePresenter
import com.pickth.habit.base.mvp.BaseView
import com.pickth.habit.view.main.adapter.MainAdapterContract
import com.pickth.habit.view.main.adapter.item.Habit

/**
 * Created by yonghoon on 2017-08-09
 */

interface MainContract {
    interface View: BaseView<Presenter> {
        fun showToast(msg: String)
        fun showAddHabitDialog()
        fun showModifyHabitDialog(position: Int, habit: Habit)
        fun scrollToLastItem()
        fun getContext(): Context
        fun updateWidget()
    }

    interface Presenter: BasePresenter {
        fun setAdapterView(view: MainAdapterContract.View)
        fun setAdapterModel(model: MainAdapterContract.Model)
        fun setTouchHelper(habitTouchHelper: ItemTouchHelper)
        fun addHabitItem(item: Habit)
        fun addHabitItems(list: ArrayList<Habit>)
        fun clearHabitItems()
        fun moveHabitItem(startPosition: Int, endPosition: Int)
        fun getItemCount(): Int
        fun changeItem(position: Int, habit: Habit)
        fun refreshAllData()
    }
}