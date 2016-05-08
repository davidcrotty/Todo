package com.david.todo.view.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.david.todo.R

/**
 * Created by DavidHome on 08/05/2016.
 */
class SpeechView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    init {
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val d = resources.getDrawable(R.drawable.speech_container);
        d.setBounds(0, 0, canvas!!.width, canvas!!.height);
        d.draw(canvas);
    }
}