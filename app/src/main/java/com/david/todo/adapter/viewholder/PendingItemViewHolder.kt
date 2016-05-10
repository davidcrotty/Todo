package com.david.todo.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.david.todo.R

/**
 * Created by DavidHome on 06/05/2016.
 */
class PendingItemViewHolder(view: View)  : RecyclerView.ViewHolder(view) {
    var taskText: TextView
    var dragHandle: ImageView
    var doneImage: ImageView
    var topBorder: View
    var taskForeground: FrameLayout
    var taskBackground: FrameLayout
    val viewType: HolderType = HolderType.PENDING

    init {
        taskText = view.findViewById(R.id.text_item) as TextView
        dragHandle = view.findViewById(R.id.drag_handle) as ImageView
        doneImage = view.findViewById(R.id.done_image) as ImageView
        topBorder = view.findViewById(R.id.top_border) as View
        taskForeground = view.findViewById(R.id.task_foreground) as FrameLayout
        taskBackground = view.findViewById(R.id.task_background) as FrameLayout
    }
}