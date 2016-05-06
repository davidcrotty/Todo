package com.david.todo.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.david.todo.R

/**
 * Created by DavidHome on 06/05/2016.
 */
class CompletedItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var taskText: TextView
    var undoButtonText: TextView
    val viewType: HolderType = HolderType.COMPLETED

    init {
        taskText = view.findViewById(R.id.completed_item_text) as TextView
        undoButtonText = view.findViewById(R.id.undo_button_text) as TextView
    }
}