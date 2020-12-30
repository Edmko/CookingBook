package com.edmko.cookingbook.ui.recipes

import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout

class HideSearchScrollingBehavior : CoordinatorLayout.Behavior<View>() {

    private var height = 0
    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        val paramsCompat: ViewGroup.MarginLayoutParams =
            child.layoutParams as ViewGroup.MarginLayoutParams
        height = child.measuredHeight + paramsCompat.topMargin
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        if (dyConsumed > height   || -dyConsumed>height )
            child.translationY = dyConsumed.toFloat()
    }
}

