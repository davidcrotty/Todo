package com.david.todo.view.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import butterknife.bindView
import com.david.todo.R
import com.david.todo.presenter.TaskListPresenter
import timber.log.Timber

/**
 * Created by DavidHome on 08/05/2016.
 */
class EnterItemView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), View.OnClickListener, TextWatcher {
    val sendImage: ImageView by bindView(R.id.send_button)
    val editText: EditText by bindView(R.id.add_item_text_edit)
    val dropShadow: View by bindView(R.id.drop_shadow)
    val iconColour: ColorStateList
    lateinit var _presenter: TaskListPresenter

    init {
        inflate(context, R.layout.enter_item_view, this);
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.speech_view, 0, 0);
        iconColour = typedArray.getColorStateList(R.styleable.speech_view_icon_colour)
        typedArray.recycle();
        editText.addTextChangedListener(this)
    }

    fun attachPresenter(presenter: TaskListPresenter) {
        _presenter = presenter
    }

    fun hideDropShadow() {
        dropShadow.visibility = View.INVISIBLE
    }

    fun showDropShadow() {
        dropShadow.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        _presenter?.delegateAddItemToAdapterWith(editText.text.toString())
        sendImage.setOnClickListener(null)
        editText.text.clear()
    }

    override fun afterTextChanged(editable: Editable?) {
        val enteredText = editable.toString()
        sendImage.setOnClickListener(if (enteredText == null || enteredText.isBlank()) null else this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        sendImage.setColorFilter(iconColour.defaultColor)
    }
}