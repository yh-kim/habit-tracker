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

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
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
        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            var views = RemoteViews(context?.packageName, R.layout.widget_habit)

            val habit = HabitManagement.getHabits(context!!)[0]
            views.setTextViewText(R.id.tv_widget_habit_title, habit.title)
            if(!habit.days.isEmpty()) {
                if(habit.days[0] == StringUtil.getCurrentDay())  {
                    views.setViewVisibility(R.id.iv_widget_habit_select, View.VISIBLE)
                }
            }

            appWidgetManager?.updateAppWidget(appWidgetId!!, views)
        }
    }


    /**
     * 가장 먼저 호출
     */
    override fun onReceive(context: Context, intent: Intent) {
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