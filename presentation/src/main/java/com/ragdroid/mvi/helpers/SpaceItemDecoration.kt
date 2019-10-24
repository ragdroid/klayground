package com.ragdroid.mvi.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
        private val space: Int,
        private val shouldShowLeft: Boolean = true,
        private val shouldShowRight: Boolean = true,
        private val shouldShowTop: Boolean = true,
        private val shouldShowBottom: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (shouldShowLeft)
            outRect.left = space
        if (shouldShowRight)
            outRect.right = space
        if (shouldShowBottom)
            outRect.bottom = space
        if (shouldShowTop)
            outRect.top = space
    }
}