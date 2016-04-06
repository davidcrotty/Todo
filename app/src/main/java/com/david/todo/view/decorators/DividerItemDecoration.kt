package com.david.todo.view.decorators

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView

/**
 * Created by DavidHome on 06/04/2016.
 */
class DividerItemDecoration(val context: Context, val resourceId: Int) : RecyclerView.ItemDecoration() {

    lateinit var _divider: Drawable

    init {
        _divider = ContextCompat.getDrawable(context, resourceId)
    }

    override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        val parent = parent?.let {it} ?: return //force unwrap parent else runtime exception
        var right = parent.width.minus(parent.paddingRight)
        var left = parent.paddingLeft

        var childCount = parent.childCount
        for(i in 0..childCount - 1) {
            val childView = parent.getChildAt(i)
            val params = childView.layoutParams as RecyclerView.LayoutParams

            val top = childView.bottom + params.bottomMargin
            val bottom = top + _divider?.intrinsicHeight

            _divider.setBounds(left, top, right, bottom);
            _divider.draw(c);
        }
    }
}