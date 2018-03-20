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

package com.pickth.habit.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.pickth.habit.R
import com.pickth.habit.view.main.MainActivity

/**
 * Created by yonghoon on 2018-02-06
 * Blog   : http://blog.pickth.com
 */

object HabitNotificationManager {
    // https://developer.android.com/guide/topics/ui/notifiers/notifications.html?hl=ko#CreateNotification
    fun showNoti(context: Context) {
        val resultPendingIntent = PendingIntent.getActivity(context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)

        // ui, task
        val mBuilder = NotificationCompat.Builder(context, "test").apply {
            setSmallIcon(R.drawable.ic_back)
            setAutoCancel(true)
            setTicker("tt")
            setContentTitle("테스트 입니다")
            setContentText("hi")
            setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            setContentIntent(resultPendingIntent)
            setWhen(System.currentTimeMillis())
        }

        val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("test", "name", importance)
            mNotifyMgr.createNotificationChannel(channel)
        }

        mNotifyMgr.notify(1, mBuilder.build())
    }
}