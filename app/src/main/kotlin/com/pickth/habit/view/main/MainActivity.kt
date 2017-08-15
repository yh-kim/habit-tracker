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

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.pickth.gachi.util.GridSpacingItemDecoration
import com.pickth.habit.R
import com.pickth.habit.base.activity.BaseActivity
import com.pickth.habit.util.HabitManager
import com.pickth.habit.view.dialog.AddHabitDialog
import com.pickth.habit.view.main.adapter.MainAdapter
import com.pickth.habit.widget.HabitWidget
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

        mRecyclerView = rv_main.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(GridSpacingItemDecoration(context,2, 16, false))
            recycledViewPool.setMaxRecycledViews(MainAdapter.HABIT_TYPE_ITEM, 0)
        }

        // presenter
        mPresenter = MainPresenter().apply {
            attachView(this@MainActivity)
            setAdapterView(mAdapter)
            setAdapterModel(mAdapter)

            addHabitItems(HabitManager.getHabits(this@MainActivity))
        }
    }

    override fun showToast(msg: String) {
        toast(msg)
    }

    override fun showAddHabitDialog() {
        addHabitDialog = AddHabitDialog(this, View.OnClickListener {
            addHabitDialog.addHabit()?.let {
                mPresenter.addHabitItem(it)
            }
        })
        addHabitDialog.show()
    }

    override fun scrollToLastItem() {
        mRecyclerView.smoothScrollToPosition(mPresenter.getItemCount())
    }

    override fun getContext(): Context = this

    override fun updateWidget() {
        var ids = AppWidgetManager
                .getInstance(this)
                .getAppWidgetIds(
                        ComponentName(this, HabitWidget::class.java)
                )
        var intent = Intent(this, HabitWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }
        sendBroadcast(intent)
    }
}