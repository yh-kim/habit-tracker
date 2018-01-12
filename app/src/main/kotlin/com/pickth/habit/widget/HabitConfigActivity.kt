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

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.RemoteViews
import com.pickth.habit.R
import com.pickth.habit.base.activity.BaseActivity
import com.pickth.habit.util.HabitManager
import com.pickth.habit.util.StringUtil
import kotlinx.android.synthetic.main.activity_habit_config.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
 * Created by yonghoon on 2017-08-15
 */

class HabitConfigActivity: BaseActivity() {

    private var mAppWidgetId: Int = 0
    private lateinit var mAppWidgetManager: AppWidgetManager
    private lateinit var mRemoteView: RemoteViews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_config)

        var mAdapter = HabitConfigAdapter()
        mAdapter.setOnClickListener(object: HabitConfigAdapter.OnHabitConfigClickListener {
            override fun onClick(position: Int) {
                alert("${mAdapter.getItem(position).title} ${applicationContext.getString(R.string.check_habit_name)}"){
                    yesButton {
                        // bind widget
                        var habit = HabitManager.getHabits(applicationContext)[position]
                        HabitWidgetManager.addWidget(applicationContext, mAppWidgetId, habit.id)

                        mRemoteView.setViewVisibility(R.id.pb_widget_loading, View.GONE)
                        mRemoteView.setInt(R.id.iv_widget_habit_background, "setColorFilter", habit.color)

                        // bind view
                        mRemoteView.setTextViewText(R.id.tv_widget_habit_title, habit.title)
                        if(!habit.days.isEmpty()) {
                            if(habit.days[0] == StringUtil.getCurrentDay())  {
                                mRemoteView.setViewVisibility(R.id.iv_widget_habit_select, View.VISIBLE)
                            } else {
                                mRemoteView.setViewVisibility(R.id.iv_widget_habit_select, View.GONE)
                            }
                        } else {
                            mRemoteView.setViewVisibility(R.id.iv_widget_habit_select, View.GONE)
                        }

                        // click event
                        // PendingIntent.getBroadcast로 하는 이유는 broadcast가 아닌 activity로 하면 터치가 안먹힘
                        var listener = PendingIntent.getBroadcast(
                                applicationContext,
                                0,
                                Intent(applicationContext, HabitWidget::class.java).apply {
                                    action = HabitWidget.ACTION_CLICKED + mAppWidgetId
                                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
                                },
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        mRemoteView.setOnClickPendingIntent(R.id.fl_widget_habit, listener)

                        // update view
                        mAppWidgetManager
                                .updateAppWidget(mAppWidgetId, mRemoteView)

                        // intent
                        var resultIntent = Intent()
                        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                    noButton {  }
                }.show()

            }

        })
        rv_habit_config.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        for(item in HabitManager.getHabits(this)) mAdapter.addItem(item)

        // widget
        var mExtras = intent.extras
        if(mExtras != null) mAppWidgetId = mExtras.
                getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        mAppWidgetManager = AppWidgetManager.getInstance(this)
        mRemoteView = RemoteViews(this.packageName, R.layout.widget_habit)
    }
}