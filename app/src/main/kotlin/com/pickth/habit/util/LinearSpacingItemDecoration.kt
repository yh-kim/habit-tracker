package com.pickth.habit.util

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.pickth.habit.extensions.convertDpToPixel

/**
 * @param spanCount number of span count
 * @param spacing dp value
 */
class LinearSpacingItemDecoration(context: Context, var spacing: Int, val includeEdge: Boolean): RecyclerView.ItemDecoration() {
    init {
        spacing = context.convertDpToPixel(spacing)
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {

        val position = parent!!.getChildAdapterPosition(view) // item position
        val column = position % 1 // item column

        if (includeEdge) {
            outRect!!.left = spacing - column * spacing // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing // (column + 1) * ((1f / spanCount) * spacing)
            if (position < 1) {
                outRect.top = spacing * 2
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect!!.left = column * spacing // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= 1) {
                outRect.top = spacing // item top
            }
        }
    }
}