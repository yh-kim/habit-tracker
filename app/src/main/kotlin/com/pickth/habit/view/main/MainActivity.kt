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
import android.content.ClipData
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.pickth.habit.R
import com.pickth.habit.base.activity.BaseActivity
import com.pickth.habit.listener.OnHabitMoveListener
import com.pickth.habit.util.HabitManager
import com.pickth.habit.util.HabitTouchHelperCallback
import com.pickth.habit.util.LinearSpacingItemDecoration
import com.pickth.habit.util.StringUtil
import com.pickth.habit.view.dialog.AddHabitDialog
import com.pickth.habit.view.dialog.ImportHabitDialog
import com.pickth.habit.view.main.adapter.MainAdapter
import com.pickth.habit.view.main.adapter.item.Habit
import com.pickth.habit.widget.HabitWidget
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton


/**
 * Created by yonghoon on 2017-08-09
 */

class MainActivity: BaseActivity(), MainContract.View {

    // firebase
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

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

        // Obtain the Firebase Analytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // actionbar
        setSupportActionBar(main_toolbar)

        // adapter
        mAdapter = MainAdapter()
        mAdapter.notifyDataSetChanged()

        mRecyclerView = rv_main.apply {
            adapter = mAdapter
            recycledViewPool.setMaxRecycledViews(0, 0)

            // linear
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(LinearSpacingItemDecoration(context,8, true))
            // grid
//            layoutManager = GridLayoutManager(context, 2)
//            addItemDecoration(GridSpacingItemDecoration(context,2, 16, false))
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
        unregisterReceiver(mChangeDateBroadcastReceiver)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.habit_add -> {
                showAddHabitDialog()
            }
            R.id.habit_export -> {
                val habits = mPresenter.getHabitsWithJson()

                // copy
                val clipboardManager = this.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("label", habits)
                clipboardManager.primaryClip = clipData

                // share
                val habitShareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, habits)
                    type = "text/plain"
                }

                startActivity(habitShareIntent)
            }
            R.id.habit_import -> {
                importHabitDialog = ImportHabitDialog(this, View.OnClickListener {
                    val habits = importHabitDialog.getHabits()
                    if(habits != null) {
                        for(habit in habits) {
                            mPresenter.addHabitItem(habit)
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

    override fun onStart() {
        super.onStart()
        if(getSharedPreferences("habits", Context.MODE_PRIVATE).getBoolean("active", false)) {
            finish()
        } else {
            getSharedPreferences("habits", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("active", true)
                    .apply()
        }

    }

    override fun onStop() {
        getSharedPreferences("habits", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("active", false)
                .apply()
        super.onStop()
    }
}