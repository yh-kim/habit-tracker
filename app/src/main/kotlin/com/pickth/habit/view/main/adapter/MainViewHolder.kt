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
import android.support.v7.widget.RecyclerView
import android.view.View
import com.pickth.habit.R
import com.pickth.habit.extensions.setShowAlphaAnimation
import com.pickth.habit.util.OnHabitClickListener
import kotlinx.android.synthetic.main.item_habit.view.*

/**
 * Created by yonghoon on 2017-08-09
 */

class MainViewHolder(view: View, val listener: OnHabitClickListener): RecyclerView.ViewHolder(view) {

    fun onBind(item: Habit, position: Int) {
        with(itemView) {
            tv_item_habit_title.text = item.title

            val back = iv_item_habit_background.background as LayerDrawable
            val background = (back.findDrawableByLayerId(R.id.square_background_item) as GradientDrawable)
                    .apply {
                        setColor(item.color)
                    }

            setOnClickListener {
                listener.onItemClick(position)
                iv_item_habit_select.visibility = View.VISIBLE
                iv_item_habit_select.setShowAlphaAnimation(500)
            }
        }
    }

    fun onBindLastItem() {
        with(itemView) {
            tv_item_habit_title.visibility = View.GONE
            iv_item_habit_last.visibility = View.VISIBLE
//            val back = iv_item_habit_background.background as LayerDrawable
//            (back.findDrawableByLayerId(R.id.square_background_item) as GradientDrawable).setVisible(false, true)
//            iv_item_habit_background.backgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)

            setOnClickListener {
                listener.onLastItemClick()
            }
        }
    }
}