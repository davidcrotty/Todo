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
        canvas.drawRect(40F,0F, 150F, 40F, paint)

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true)
        val path = Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(40F, 40F);
        path.lineTo(40F, 0F);
        path.lineTo(0F, 0F);
        path.close();

        canvas.drawPath(path, paint);

//        canvas?.drawRect(30F, 30F, 80F, 80F, paint);
//        paint.setStrokeWidth(0F);
//        paint.setColor(Color.CYAN);
//        canvas?.drawRect(33F, 60F, 77F, 77F, paint );
//        paint.setColor(Color.YELLOW);
//        canvas?.drawRect(33F, 33F, 77F, 60F, paint );
    }
}