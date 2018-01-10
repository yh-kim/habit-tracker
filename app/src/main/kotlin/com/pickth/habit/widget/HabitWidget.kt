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

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.pickth.habit.R
import com.pickth.habit.util.HabitManager
import com.pickth.habit.util.StringUtil
import android.content.ComponentName



/**
 * Created by yonghoon on 2017-08-14
 */

class HabitWidget: AppWidgetProvider() {
    val TAG = "${javaClass.simpleName}"

    companion object {
        val HABIT_CLICK = "android.action.HABIT_CLICK"
        val ACTION_CLICKED = "com.pickth.habit.CLICKED_"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            Log.v("HabitWidget", "Update widgets")
            // bind
            val views = RemoteViews(context.packageName, R.layout.widget_habit)

            views.setViewVisibility(R.id.pb_widget_loading, View.GONE)

            val position = HabitWidgetManager.getHabitPosition(context, appWidgetId)

            if(position == null) {
                return
            }

            var habit = HabitManager.getHabits(context)[position]
            views.setInt(R.id.iv_widget_habit_background, "setColorFilter", habit.color)

            views.setTextViewText(R.id.tv_widget_habit_title, habit.title)
            if(!habit.days.isEmpty()) {
                if(habit.days[0] == StringUtil.getCurrentDay())  {
                    views.setViewVisibility(R.id.iv_widget_habit_select, View.VISIBLE)
                } else {
                    views.setViewVisibility(R.id.iv_widget_habit_select, View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.iv_widget_habit_select, View.GONE)
            }

            // click event
            var listener = PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(context, HabitWidget::class.java).apply {
                        action = HabitWidget.ACTION_CLICKED + appWidgetId
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.fl_widget_habit, listener)

            // update view
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


    /**
     * 가장 먼저 호출
     */
    override fun onReceive(context: Context, intent: Intent) {
        Log.v(TAG, "Widget onReceive")
        var views = RemoteViews(context.packageName, R.layout.widget_habit)
        if (intent.action.startsWith(ACTION_CLICKED)) {
            var id = intent.action.substring(ACTION_CLICKED.length).toInt()
            val habitPosition = HabitWidgetManager.getHabitPosition(context, id)

            if(habitPosition == null) {
                return
            }

            val habit = HabitManager.getHabits(context)[habitPosition]
            views.setInt(R.id.iv_widget_habit_background, "setColorFilter", habit.color)
            if (!habit.days.isEmpty()) {
                if (habit.days[0] == StringUtil.getCurrentDay()) {
                    // 체크 되어있는 상태
                    habit.days.removeAt(0)
                    HabitManager.notifyDataSetChanged(context)
                    views.setViewVisibility(R.id.iv_widget_habit_select, View.GONE)
                } else {
                    // 체크 안 되어있는 상태
                    habit.days.add(0, StringUtil.getCurrentDay())
                    HabitManager.notifyDataSetChanged(context)
                    views.setViewVisibility(R.id.iv_widget_habit_select, View.VISIBLE)
                }
            } else {
                // 비어있으므로 체크
                habit.days.add(0, StringUtil.getCurrentDay())
                HabitManager.notifyDataSetChanged(context)
                views.setViewVisibility(R.id.iv_widget_habit_select, View.VISIBLE)
            }

            AppWidgetManager.getInstance(context)
                    .updateAppWidget(id, views)
        } else if(Intent.ACTION_DATE_CHANGED == intent.action) {
            Log.v(TAG, "ACTION DATE CHANGED, date: ${StringUtil.getCurrentDay()}")
            // update all widgets
            val thisWidget = ComponentName(context, HabitWidget::class.java)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            if(appWidgetIds != null) {
                this.onUpdate(context,appWidgetManager , appWidgetIds)
            }
        }
        super.onReceive(context, intent)
    }

    /**
     * 처음 위젯을 생성했을 때 한 번만 호출됨
     */
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    /**
     * 위젯을 갱신할 때 호출
     * 처음 등록할 때는 호출되지 않음
     * widget_configuration.xml 에서 updatePeriodMillis 설정한 시간 간격으로 호출하는 듯
     * 지금은 0이기 때문에 MainActivity에서 habit 클릭할 때마다 호출
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.v(TAG, "Widget onUpdate")
        for(appWidgetId in appWidgetIds) updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    /**
     * 위젯이 삭제될 때 호출
     */
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }

    /**
     * 마지막 남은 위젯이 삭제될 때 호출
     */
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }
}