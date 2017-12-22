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

package com.pickth.habit.view.dialog

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pickth.habit.R
import com.pickth.habit.listener.OnHabitClickListener
import com.pickth.habit.view.main.adapter.item.Habit
import kotlinx.android.synthetic.main.item_habit.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
 * Created by yonghoon on 2017-09-20
 * Blog   : http://blog.pickth.com
 */

class ImportHabitAdapter: RecyclerView.Adapter<ImportHabitAdapter.ImportHabitViewHolder>() {
    private var mItems = ArrayList<Habit>()

    private lateinit var mListener: OnHabitClickListener

    override fun onBindViewHolder(holder: ImportHabitViewHolder, position: Int) {
        holder.onBind(mItems[position], position)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ImportHabitViewHolder {
        val itemView = LayoutInflater
                .from(parent?.context)
                .inflate(R.layout.item_habit, parent, false)

        return ImportHabitViewHolder(itemView, mListener)
    }

    fun setOnHabitClickListener(listener: OnHabitClickListener) {
        mListener = listener
    }

    fun addItem(item: Habit) {
        mItems.add(item)
        notifyItemInserted(itemCount-1)
    }

    fun addItems(list: ArrayList<Habit>) {
        for(i in list) addItem(i)
    }

//    override fun getItemViewType(position: Int): Int {
//        return 0
//    }

    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int): Boolean {
        if(mItems.isEmpty()) return false

        mItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        return true
    }

    fun getItem(position: Int): Habit = mItems[position]

    fun getItems(): ArrayList<Habit> = mItems

    class ImportHabitViewHolder(view: View, val listener: OnHabitClickListener) : RecyclerView.ViewHolder(view) {
        fun onBind(item: Habit, position: Int) {
            with(itemView) {
                tv_item_habit_title.text = item.title
                iv_item_habit_select.visibility = View.GONE

                setOnLongClickListener {
                    context.alert("정말 삭제하시겠습니까?") {
                        yesButton { listener.onItemLongClick(position) }
                        noButton { }
                    }.show()
                    true
                }
            }
        }
    }
}