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
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.pickth.habit.R
import com.pickth.habit.util.HabitManagement
import com.pickth.habit.util.StringUtil

/**
 * Created by yonghoon on 2017-08-14
 */

class HabitWidget: AppWidgetProvider() {

    companion object {
        val HABIT_CLICK = "android.action.HABIT_CLICK"

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

            // bind
            var views = RemoteViews(context.packageName, R.layout.widget_habit)
            val habit = HabitManagement.getHabits(context)[0]
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
                    Intent(HABIT_CLICK),
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.fl_widget_habit, listener)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


    /**
     * 가장 먼저 호출
     */
    override fun onReceive(context: Context, intent: Intent) {
        var views = RemoteViews(context.packageName, R.layout.widget_habit)

        when(intent.action) {
            HABIT_CLICK -> {
                val habit = HabitManagement.getHabits(context)[0]
                if(!habit.days.isEmpty()) {
                    if(habit.days[0] == StringUtil.getCurrentDay())  {
                        // 체크 되어있는 상태
                        habit.days.removeAt(0)
                        HabitManagement.notifyDataSetChanged(context)
                        views.setViewVisibility(R.id.iv_widget_habit_select, View.GONE)
                    } else {
                        // 체크 안 되어있는 상태
                        habit.days.add(0, StringUtil.getCurrentDay())
                        HabitManagement.notifyDataSetChanged(context)
                        views.setViewVisibility(R.id.iv_widget_habit_select, View.VISIBLE)
                    }
                } else {
                    // 비어있으므로 체크
                    habit.days.add(0, StringUtil.getCurrentDay())
                    HabitManagement.notifyDataSetChanged(context)
                    views.setViewVisibility(R.id.iv_widget_habit_select, View.VISIBLE)
                }
            }
        }

        AppWidgetManager.getInstance(context)
                .updateAppWidget(ComponentName(context, javaClass), views)

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
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
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