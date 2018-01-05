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
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.pickth.habit.R
import com.pickth.habit.extensions.setHideAlphaAnimation
import com.pickth.habit.extensions.setShowAlphaAnimation
import com.pickth.habit.listener.HabitTouchHelperViewHolder
import com.pickth.habit.listener.OnHabitTouchListener
import com.pickth.habit.listener.OnHabitDragListener
import com.pickth.habit.util.StringUtil
import com.pickth.habit.view.main.adapter.item.Habit
import com.pickth.habit.view.main.adapter.item.PlusHabit
import kotlinx.android.synthetic.main.item_habit.view.*
import org.jetbrains.anko.*

/**
 * Created by yonghoon on 2017-08-09
 */

class MainViewHolder(view: View, val listener: OnHabitTouchListener, val dragListener: OnHabitDragListener) : RecyclerView.ViewHolder(view), HabitTouchHelperViewHolder {
    private var isDrag = false

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
            if (item is PlusHabit) {
                tv_item_habit_title.visibility = View.GONE
                tv_item_habit_day.visibility = View.GONE
                iv_item_habit_last.visibility = View.VISIBLE

                setOnClickListener {
                    listener.onLastItemClick()
                }
            } else {
                // 일반 아이템
                var isCheck = false

                // title
                tv_item_habit_title.text = item.title
                tv_item_habit_title_drag.text = item.title

                // day
                if(item.days.size != 0) tv_item_habit_day.text = item.days[0]
                if(!item.days.isEmpty()) {
                    var day = StringUtil.formatDayToString(item.days[0])
                    var textDay = ""
                    if(day == "0") {
                        textDay = context.getString(R.string.habit_today)
                    } else {
                        textDay = day + context.getString(R.string.habit_days_ago)
                    }
                    tv_item_habit_day.text = textDay

                    isCheck = item.days[0] == StringUtil.getCurrentDay()
                }

                // check
                if(isCheck) iv_item_habit_select.visibility = View.VISIBLE


                setOnClickListener {
                    if (isCheck) {
                        context.alert(context.getString(R.string.check_cancel)) {
                            yesButton {
                                listener.onItemUnCheck(position)

                                iv_item_habit_select.visibility = View.INVISIBLE
                                iv_item_habit_select.setHideAlphaAnimation(500)
                            }
                            noButton { }
                        }.show()

                    } else {
                        // 체크했을 때
                        listener.onItemCheck(position)

                        iv_item_habit_select.visibility = View.VISIBLE
                        iv_item_habit_select.setShowAlphaAnimation(500)
                    }

                }

                setOnLongClickListener {
//                    val longClickItem = listOf("이동", "수정", "삭제", "취소")
                    context.selector(null, resources.getStringArray(R.array.habit_actions).toList(), {
                        _,
                        i -> when(i) {
                            0 -> {
                                isDrag = true
                                iv_item_habit_drag.visibility = View.VISIBLE
                                tv_item_habit_title_drag.visibility = View.VISIBLE
                                iv_item_habit_drag_icon.visibility = View.VISIBLE
                                context.toast(context.getString(R.string.move_habit))
//                                dragListener.onStartDrag(this@MainViewHolder)
                            }
                            1 -> {
                                listener.onItemModify(position, item)
                             }
                            2 -> context.alert(context.getString(R.string.check_delete)) {
                                        yesButton { listener.onItemRemove(position) }
                                        noButton { }
                            }.show()
                        }
                    })
                    true
                }
            }

            setOnTouchListener { v, motionEvent ->
                if ((MotionEventCompat.getActionMasked(motionEvent) ==
                        MotionEvent.ACTION_DOWN )&& isDrag) {
                    dragListener.onStartDrag(this@MainViewHolder)
                }
                isDrag = false
                false
            }
        }
    }

    /**
     * Called when pressing in drag mode
     */
    override fun onItemSelected() {
    }

    override fun onItemClear() {
        itemView.iv_item_habit_drag.visibility = View.GONE
        itemView.tv_item_habit_title_drag.visibility = View.GONE
        itemView.iv_item_habit_drag_icon.visibility = View.GONE
//        val back = itemView.iv_item_habit_background.background as LayerDrawable
//        (back.findDrawableByLayerId(R.id.square_background_item) as GradientDrawable)
//                .run {
//                    setColor(ContextCompat.getColor(itemView.context, R.color.colorPlus))
//                }
//        itemView.setBackgroundColor(0)
    }
}