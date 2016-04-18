package com.david.todo.view.eventlisteners

import android.support.v7.widget.RecyclerView

/**
 * Created by DavidHome on 18/04/2016.
 */
interface IHandleListener {
    fun onHandleDown(viewHolder: RecyclerView.ViewHolder)
}