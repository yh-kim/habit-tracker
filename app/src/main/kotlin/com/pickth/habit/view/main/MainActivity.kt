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

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.pickth.gachi.util.GridSpacingItemDecoration
import com.pickth.habit.R
import com.pickth.habit.base.activity.BaseActivity
import com.pickth.habit.util.HabitManagement
import com.pickth.habit.view.dialog.AddHabitDialog
import com.pickth.habit.view.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

/**
 * Created by yonghoon on 2017-08-09
 */

class MainActivity: BaseActivity(), MainContract.View {

    private lateinit var mPresenter: MainPresenter
    private lateinit var mAdapter: MainAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var addHabitDialog: AddHabitDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // actionbar
        setSupportActionBar(main_toolbar)

        // adapter
        mAdapter = MainAdapter()
        mAdapter.notifyDataSetChanged()

        // presenter
        mPresenter = MainPresenter().apply {
            attachView(this@MainActivity)
            setAdapterView(mAdapter)
            setAdapterModel(mAdapter)
        }

        mRecyclerView = rv_main.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(GridSpacingItemDecoration(context,2, 16, false))
            recycledViewPool.setMaxRecycledViews(MainAdapter.HABIT_TYPE_ITEM, 0)
        }

//        HabitManagement.removeAllHabit(this)
        mPresenter.addHabitItems(HabitManagement.getHabits(this))
    }

    override fun showToast(msg: String) {
        toast(msg)
    }

    override fun showAddHabitDialog() {
        addHabitDialog = AddHabitDialog(this, View.OnClickListener {
            var habit = addHabitDialog.addHabit()
            if(habit != null) {
                mPresenter.addHabitItem(habit)
                HabitManagement.addHabit(this, habit)
            }
        })

        addHabitDialog.show()
//        var habit = Habit(UUID.randomUUID().toString(),"습관${mPresenter.getItemCount() + 1}", ContextCompat.getColor(this, R.color.colorAccent), false, ArrayList(), false)
//        mPresenter.addHabitItem(habit)
//        HabitManagement.addHabit(this, habit)
    }

    override fun scrollToLastItem() {
        mRecyclerView.smoothScrollToPosition(mPresenter.getItemCount())
    }
}