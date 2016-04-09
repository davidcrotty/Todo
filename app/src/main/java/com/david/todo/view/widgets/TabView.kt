package com.david.todo.view.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by DavidHome on 07/04/2016.
 */
class TabView(context: Context, attributes: AttributeSet) : View(context, attributes) {

    lateinit var paint: Paint

    init {
        paint = Paint()
    }

    override fun onDraw(canvas: Canvas?) {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3F);
        canvas?.drawRect(30F, 30F, 80F, 80F, paint);
        paint.setStrokeWidth(0F);
        paint.setColor(Color.CYAN);
        canvas?.drawRect(33F, 60F, 77F, 77F, paint );
        paint.setColor(Color.YELLOW);
        canvas?.drawRect(33F, 33F, 77F, 60F, paint );
    }
}