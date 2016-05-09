package com.david.todo.view.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import butterknife.bindView
import com.david.todo.R

/**
 * Created by DavidHome on 08/05/2016.
 */
class EnterItemView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    val sendImage: ImageView by bindView(R.id.send_button)
    val iconColour: ColorStateList

    init {
        inflate(context, R.layout.enter_item_view, this);
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.speech_view, 0, 0);
        iconColour = typedArray.getColorStateList(R.styleable.speech_view_icon_colour)
        typedArray.recycle();
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        sendImage.setColorFilter(iconColour.defaultColor)
    }
}