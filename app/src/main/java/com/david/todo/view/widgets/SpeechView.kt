package com.david.todo.view.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.david.todo.R

/**
 * Created by DavidHome on 08/05/2016.
 */
class SpeechView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    lateinit var triangle: Path
    lateinit var fill: Paint

    init {
        triangle = Path()
        triangle.fillType = Path.FillType.EVEN_ODD
        fill = Paint()
        fill.color = context.resources.getColor(android.R.color.white);
        fill.strokeWidth = 1F;
        fill.style = Paint.Style.FILL_AND_STROKE
        fill.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val speechBubble = resources.getDrawable(R.drawable.speech_container);
        speechBubble.setBounds(0, 0, canvas!!.width - 48, canvas!!.height);
        speechBubble.draw(canvas);

        triangle.moveTo(canvas!!.width.toFloat(), 0F)
        triangle.lineTo(canvas!!.width.toFloat(), 0F)
        triangle.lineTo(canvas!!.width.toFloat() - 48F, 0F)
        triangle.lineTo(canvas!!.width.toFloat() - 48F, 50F)
        triangle.close()

        canvas.drawPath(triangle, fill)
    }
}