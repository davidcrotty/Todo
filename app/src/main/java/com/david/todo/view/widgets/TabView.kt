package com.david.todo.view.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.david.todo.R
import timber.log.Timber

/**
 * Created by DavidHome on 07/04/2016.
 */
class TabView(context: Context, attributes: AttributeSet) : View(context, attributes) {

    lateinit var paint: Paint
    lateinit var shade: Path
    var viewHeight: Int = 0
    var viewWidth: Int = 0
    var color: Int
    var curvePaint: Paint

    init {
        paint = Paint()
        shade = Path()
        color = context.resources.getColor(R.color.red)

        val curveColour = context.resources.getColor(R.color.green)
        curvePaint = Paint()
        curvePaint.color = curveColour
        curvePaint.strokeWidth = 3F;
        curvePaint.style = Paint.Style.FILL_AND_STROKE
        curvePaint.isAntiAlias = true
    }

    fun setColour(colourHex: Int) {
        color = colourHex
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(viewWidth, viewHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    override fun onDraw(canvas: Canvas?) {
        Timber.d("Height: $viewHeight Width: $viewWidth")

        val canvas = canvas?.let { it } ?: return
        paint.color = color;
        paint.strokeWidth = 3F;
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.isAntiAlias = true

        var path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        //Should start at 0,0 anyway
        path.lineTo(0F, viewHeight.toFloat())
        path.lineTo(viewWidth.toFloat() / 1.5F, viewHeight.toFloat())
        path.close()

//        path = Path()
//        path.moveTo(viewWidth.toFloat() / 2F, viewHeight.toFloat())
//        val oval = RectF()

        canvas.drawPath(path, paint)
    }
}