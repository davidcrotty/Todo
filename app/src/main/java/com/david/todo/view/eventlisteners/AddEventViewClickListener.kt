package com.david.todo.view.eventlisteners

import android.view.View
import com.david.todo.presenter.AddItemPresenter

/**
 * Created by DavidHome on 02/04/2016.
 */
class AddEventViewClickListener(val addItemPresenter: AddItemPresenter) : View.OnClickListener {

    override fun onClick(v: View?) {
        addItemPresenter.delegateAddEventView();
    }

}
