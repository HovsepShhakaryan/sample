package com.vylo.common.adapter.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    private val left: Int = 0,
    private val top: Int = 0,
    private val right: Int = 0,
    private val bottom: Int = 0,
    private val isTopMargin: Boolean = false,
    private val isBottomMargin: Boolean = true
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.let {
            if (isTopMargin || parent.getChildAdapterPosition(view) == 0) {
                it.top = top
            }

            it.left = left
            it.right = right

            if (isBottomMargin || parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(
                    1
                )
            ) {
                it.bottom = bottom
            }
        }
    }
}