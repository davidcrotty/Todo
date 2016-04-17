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

    lateinit var peelUnderShape: Path
    lateinit var peelOverShape: Path
    lateinit var shadowCurve: Path

    lateinit var peelUnderPaint: Paint
    lateinit var curvePaint: Paint
    lateinit var shadowPaint: Paint

    var viewHeight: Int = 0
    var viewWidth: Int = 0

    init {
        createPaintEffects()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(viewWidth, viewHeight);
        createDrawCoordinates()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    override fun onDraw(canvas: Canvas?) {
        Timber.d("Height: $viewHeight Width: $viewWidth")

        canvas?.drawPath(peelUnderShape, peelUnderPaint)
        canvas?.drawPath(peelOverShape, curvePaint)
        canvas?.drawPath(shadowCurve, shadowPaint)
    }

    fun createPaintEffects() {
        peelUnderPaint = Paint()
        peelUnderPaint.color = context.resources.getColor(R.color.green);
        peelUnderPaint.strokeWidth = 3F;
        peelUnderPaint.style = Paint.Style.FILL_AND_STROKE
        peelUnderPaint.isAntiAlias = true

        curvePaint = Paint()
        curvePaint.color = context.resources.getColor(R.color.green_peel)
        curvePaint.strokeWidth = 3F;
        curvePaint.style = Paint.Style.FILL_AND_STROKE
        curvePaint.isAntiAlias = true

        shadowPaint = Paint()
        shadowPaint.color = context.resources.getColor(android.R.color.black)
        shadowPaint.strokeWidth = 0F;
        shadowPaint.style = Paint.Style.STROKE
        shadowPaint.isAntiAlias = true
        // set shadow
        shadowPaint.setShadowLayer(15F, -10F, -10F, Color.BLACK);
        // Important for certain APIs
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
    }

    fun createDrawCoordinates() {
        peelUnderShape = Path()
        peelUnderShape.fillType = Path.FillType.EVEN_ODD
        //Paths start at 0,0 by default
        peelUnderShape.lineTo(0F, viewHeight.toFloat())
        peelUnderShape.lineTo(viewWidth.toFloat() / 1.5F, viewHeight.toFloat())
        peelUnderShape.close()

        peelOverShape = Path()
        peelOverShape.moveTo(0F, 0F)
        peelOverShape.quadTo(viewWidth.toFloat() / 2, //high point of curve
                40F, //depth of curve
                viewWidth.toFloat() - (viewWidth.toFloat() / 4), //width
                20F) //height, 0 is top

        peelOverShape.quadTo(40F,
                50F,
                viewWidth.toFloat() / 1.5F,
                viewHeight.toFloat())

        peelOverShape.quadTo(15F,
                viewHeight.toFloat() / 2F,
                0F,
                0F)
        peelOverShape.close()

        shadowCurve = Path()
        shadowCurve.moveTo(viewWidth.toFloat() / 1.5F, viewHeight.toFloat())
        shadowCurve.quadTo(15F,
                viewHeight.toFloat() / 2F,
                0F,
                0F)
    }
}