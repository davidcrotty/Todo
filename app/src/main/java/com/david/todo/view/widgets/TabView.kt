package com.david.todo.view.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.david.todo.R

/**
 * Created by DavidHome on 07/04/2016.
 */
class TabView(context: Context, attributes: AttributeSet) : View(context, attributes) {

    lateinit var paint: Paint
    var color: Int

    init {
        paint = Paint()
        color = context.resources.getColor(R.color.red)
    }

    fun setColour(colourHex: Int) {
        color = colourHex
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        val canvas = canvas?.let { it } ?: return
        paint.color = color;
        paint.strokeWidth = 3F;
        canvas.drawRect(60F,0F, 250F, 60F, paint)

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true
        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.lineTo(60F, 60F)
        path.lineTo(60F, 6F)
        path.lineTo(0F, 0F)
        path.close()

        canvas.drawPath(path, paint)
    }
}