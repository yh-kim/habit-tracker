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

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.pickth.habit.listener.HabitTouchHelperViewHolder
import com.pickth.habit.listener.OnHabitMoveListener

/**
 * Created by yonghoon on 2017-12-11
 * Blog   : http://blog.pickth.com
 *
 *
 * https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd
 */
class HabitTouchHelperCallback(val habitMoveListener: OnHabitMoveListener): ItemTouchHelper.Callback() {

    /**
     * 어느 방향으로 움직일 것인지
     */
    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        var dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        var swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END   // no swipe action : 0
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//        if(recyclerView.adapter.itemCount == viewHolder.adapterPosition) return false

        habitMoveListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    /**
     * Called by the state of a View Holder changes to drag or swipe
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if(viewHolder is HabitTouchHelperViewHolder) {
                viewHolder.onItemSelected()
            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)
        if(viewHolder is HabitTouchHelperViewHolder) {
            viewHolder.onItemClear()
        }
    }


}