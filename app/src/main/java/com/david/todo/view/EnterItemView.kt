package com.david.todo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.david.todo.R

/**
 * Created by DavidHome on 08/05/2016.
 */
class EnterItemView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    init {
        inflate(context, R.layout.enter_item_view, this);
    }
}