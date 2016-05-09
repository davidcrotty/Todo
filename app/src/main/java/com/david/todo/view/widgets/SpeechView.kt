package com.david.todo.view.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
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

    val triangleArea: Int = 48
    val triangleBase: Float = 0F

    lateinit var iconColour: ColorStateList
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
        val speechBubble = resources.getDrawable(R.drawable.speech_container)
        speechBubble.setBounds(0, 0, canvas!!.width - triangleArea, canvas!!.height)
        speechBubble.draw(canvas);

        triangle.moveTo(canvas!!.width.toFloat(), triangleBase)
        triangle.lineTo(canvas!!.width.toFloat(), triangleBase)
        triangle.lineTo(canvas!!.width.toFloat() - triangleArea, triangleBase)
        triangle.lineTo(canvas!!.width.toFloat() - triangleArea, triangleArea.toFloat())
        triangle.close()

        canvas.drawPath(triangle, fill)
    }
}