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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pickth.habit.R
import com.pickth.habit.util.OnHabitClickListener

/**
 * Created by yonghoon on 2017-08-09
 */

class MainAdapter: RecyclerView.Adapter<MainViewHolder>(), MainAdapterContract.View, MainAdapterContract.Model {

    private var mItems = ArrayList<Habit>().apply {
        add(Habit("",0, false))
    }
    private lateinit var mListener: OnHabitClickListener

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater
                .from(parent?.context)
                .inflate(R.layout.item_habit, parent, false)

        return MainViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
        if(position == itemCount - 1) {
            // 마지막
            holder?.onBindLastItem()
        } else {
            holder?.onBind(mItems[position], position)
        }
    }

    override fun getItemCount(): Int = mItems.size

    override fun setOnHabitClickListener(listener: OnHabitClickListener) {
        mListener = listener
    }

    override fun addItem(item: Habit) {
        mItems.add(itemCount - 1, item)
//        notifyDataSetChanged()
        notifyItemInserted(itemCount - 1)
    }
}