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

package com.pickth.habit.view.main.adapter

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.pickth.habit.R
import com.pickth.habit.extensions.setHideAlphaAnimation
import com.pickth.habit.extensions.setShowAlphaAnimation
import com.pickth.habit.util.HabitManagement
import com.pickth.habit.util.OnHabitClickListener
import kotlinx.android.synthetic.main.item_habit.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
 * Created by yonghoon on 2017-08-09
 */

class MainViewHolder(view: View, val listener: OnHabitClickListener) : RecyclerView.ViewHolder(view) {

    fun onBind(item: Habit, position: Int) {
        with(itemView) {
            val back = iv_item_habit_background.background as LayerDrawable
            (back.findDrawableByLayerId(R.id.square_background_item) as GradientDrawable)
                    .run {
                        if (item.color == 0) {
                            setColor(ContextCompat.getColor(context, R.color.colorPlus))
                        } else {
                            setColor(item.color)
                        }
                    }

            // itemView is plus button
            if (item.isLast) {
                tv_item_habit_title.visibility = View.GONE
                tv_item_habit_day.visibility = View.GONE
                iv_item_habit_last.visibility = View.VISIBLE

                setOnClickListener {
                    listener.onLastItemClick()
                }
            } else {
                // 일반 아이템
                tv_item_habit_title.text = item.title
                if(item.days.size != 0) tv_item_habit_day.text = item.days[0]
                if (item.isCheck) iv_item_habit_select.visibility = View.VISIBLE

                setOnLongClickListener {
                    Log.v("habit000", "$position")
                    context.alert("정말 삭제하시겠습니까?") {
                        yesButton { listener.onItemLongClick(position) }
                        noButton { }
                    }.show()

                    true
                }

                setOnClickListener {
                    if (item.isCheck) {
                        context.alert("정말 취소하시겠습니까?") {
                            yesButton {
                                item.isCheck = false
                                HabitManagement.notifyDataSetChanged(context)
                                iv_item_habit_select.visibility = View.INVISIBLE
                                iv_item_habit_select.setHideAlphaAnimation(500)
                            }
                            noButton { }
                        }.show()

                    } else {
                        // 체크했을 때
                        item.isCheck = true
                        HabitManagement.notifyDataSetChanged(context)
                        iv_item_habit_select.visibility = View.VISIBLE
                        iv_item_habit_select.setShowAlphaAnimation(500)
                    }

                }
            }


        }
    }
}