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
import android.content.*
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pickth.habit.R
import com.pickth.habit.base.activity.BaseActivity
import com.pickth.habit.util.HabitManager
import com.pickth.habit.view.dialog.AddHabitDialog
import com.pickth.habit.view.dialog.ImportHabitDialog
import com.pickth.habit.view.main.adapter.MainAdapter
import com.pickth.habit.widget.HabitWidget
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.content.ClipData
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.pickth.gachi.util.GridSpacingItemDecoration
import com.pickth.habit.listener.OnHabitMoveListener
import com.pickth.habit.util.HabitTouchHelperCallback
import com.pickth.habit.util.StringUtil
import com.pickth.habit.view.main.adapter.item.Habit
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


/**
 * Created by yonghoon on 2017-08-09
 */

class MainActivity: BaseActivity(), MainContract.View {

    private var mDay: String = ""
    private lateinit var mPresenter: MainPresenter
    private lateinit var mAdapter: MainAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var addHabitDialog: AddHabitDialog
    private lateinit var importHabitDialog: ImportHabitDialog
    private lateinit var mHabitTouchHelper: ItemTouchHelper
    private val filter: IntentFilter by lazy {
        IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
        }
    }
    private val mChangeDateBroadcastReceiver: BroadcastReceiver by lazy {
        object: BroadcastReceiver() {
            override fun onReceive(p0: Context, p1: Intent) {
                when(p1.action) {
                    Intent.ACTION_TIME_TICK -> {
                        val day = StringUtil.getCurrentDay()
                        if(mDay != day) {
                            mDay = day
                            mPresenter.refreshAllData()
                        }

                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate")

        // actionbar
        setSupportActionBar(main_toolbar)

        // adapter
        mAdapter = MainAdapter()
        mAdapter.notifyDataSetChanged()

        mRecyclerView = rv_main.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            layoutManager = GridLayoutManager(context, 2)
//            addItemDecoration(GridSpacingItemDecoration(context,2, 16, false))
            recycledViewPool.setMaxRecycledViews(MainAdapter.HABIT_TYPE_ITEM, 0)
        }

        mHabitTouchHelper = ItemTouchHelper(HabitTouchHelperCallback(object: OnHabitMoveListener {
            override fun onItemMove(startPosition: Int, endPosition: Int) {
                mPresenter.moveHabitItem(startPosition, endPosition)
            }
        }))
        mHabitTouchHelper.attachToRecyclerView(mRecyclerView)

        // presenter
        mPresenter = MainPresenter().apply {
            attachView(this@MainActivity)
            setAdapterView(mAdapter)
            setAdapterModel(mAdapter)
            setTouchHelper(mHabitTouchHelper)

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
                mPresenter.refreshAllData()
            }
        })
        addHabitDialog.show()
    }

    override fun showModifyHabitDialog(position: Int, habit: Habit) {
        addHabitDialog = AddHabitDialog(this, View.OnClickListener {
            mPresenter.changeItem(position, addHabitDialog.modifyHabit()!!)
            mPresenter.refreshAllData()
        }, habit.title, habit)
        addHabitDialog.show()
    }

    override fun scrollToLastItem() {
        mRecyclerView.smoothScrollToPosition(mPresenter.getItemCount())
    }

    override fun getContext(): Context = this

    override fun updateWidget() {
        val ids = AppWidgetManager
                .getInstance(this)
                .getAppWidgetIds(
                        ComponentName(this, HabitWidget::class.java)
                )
        val intent = Intent(this, HabitWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        }
        sendBroadcast(intent)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mChangeDateBroadcastReceiver, filter)
        if(mDay == "") {
            mDay = StringUtil.getCurrentDay()
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mChangeDateBroadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.habit_export -> {
                val habits = mPresenter.getHabitsWithJson()

                // 복사
                val clipboardManager = this.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("label", habits)
                clipboardManager.primaryClip = clipData

                // 공유
                val habitShareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, habits)
                    type = "text/plain"
                }

                startActivity(habitShareIntent)
            }
            R.id.habit_import -> {
                importHabitDialog = ImportHabitDialog(this, View.OnClickListener {
                    val habit = importHabitDialog.getHabits()
                    if(habit != null) {
                        for(item in habit) {
                            mPresenter.addHabitItem(item)
                        }
                    }

                })
                importHabitDialog.show()
            }
            R.id.habit_remove_all -> {
                getContext().alert(getString(R.string.check_delete)) {
                    yesButton { mPresenter.clearHabitItems() }
                    noButton { }
                }.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}