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
    lateinit var shadeColour: Paint
    lateinit var shade: Path
    var color: Int

    init {
        paint = Paint()
        shadeColour = Paint()
//        fixedColour.color = context.resources.getColor(R.color.orange)
        shadeColour.strokeWidth = 3F;
        //        canvas.drawRect(60F,0F, 250F, 60F, paint)

        shadeColour.style = Paint.Style.FILL_AND_STROKE
        shadeColour.isAntiAlias = true

        shade = Path()
        color = context.resources.getColor(R.color.red)
    }

    fun setColour(colourHex: Int) {
        color = colourHex
//        shadeColour = (colourHex & 0xfefefe) >> 1
        shadeColour.color = darker(colourHex, 0.95F)
        invalidate()
    }

    fun darker(color: Int, factor: Float): Int {
        var a = Color.alpha( color );
        var r = Color.red( color );
        var g = Color.green( color );
        var b = Color.blue( color );

        return Color.argb( a,
                Math.max((r * factor).toInt(), 0 ),
                Math.max((g * factor).toInt(), 0 ),
                Math.max((b * factor).toInt(), 0 ) );
    }

    override fun onDraw(canvas: Canvas?) {
        val canvas = canvas?.let { it } ?: return
        paint.color = color;
        paint.strokeWidth = 3F;
//        canvas.drawRect(60F,0F, 250F, 60F, paint)

        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true
        var path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.lineTo(0F, 0F)
        path.lineTo(250F, 0F)
        path.lineTo(60F, 60F)
        path.close()
        canvas.drawPath(path, paint)

        shade = Path()
        shade.fillType = Path.FillType.EVEN_ODD
        shade.moveTo(60F, 60F) //Line always starts at 0,0 on canvas unless moved
        shade.lineTo(60F, 60F)
        shade.lineTo(250F,0F)
        shade.lineTo(250F, 60F)
        shade.close()
        canvas.drawPath(shade, shadeColour)
    }
}