package com.david.todo.adapter.viewholder

import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.david.todo.R
import com.david.todo.view.activity.TaskListActivity

/**
 * Created by DavidHome on 06/05/2016.
 */
class PendingItemViewHolder(view: View,
                            val activity: TaskListActivity)  : RecyclerView.ViewHolder(view) {

    companion object {
        val COMPLETE_VIEW: Int = 0
        val DELETE_VIEW: Int = 1
    }

    var taskText: TextView
    var dragHandle: ImageView
    var doneImage: ImageView
    var topBorder: View
    var taskForeground: RelativeLayout
    var taskBackground: FrameLayout
    var actionSwitch: ViewSwitcher
    var deleteButton: ViewGroup
    var viewType: HolderType = HolderType.PENDING

    init {
        taskText = view.findViewById(R.id.text_item) as TextView
        dragHandle = view.findViewById(R.id.drag_handle) as ImageView
        doneImage = view.findViewById(R.id.done_image) as ImageView
        topBorder = view.findViewById(R.id.top_border) as View
        taskForeground = view.findViewById(R.id.task_foreground) as RelativeLayout
        taskBackground = view.findViewById(R.id.complete_task_background) as FrameLayout
        actionSwitch = view.findViewById(R.id.view_action_switch) as ViewSwitcher
        deleteButton = view.findViewById(R.id.delete_background) as ViewGroup

        taskText.setOnClickListener {
            activity.startSupportActionMode(object: android.support.v7.view.ActionMode.Callback {
                override fun onPrepareActionMode(mode: android.support.v7.view.ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: android.support.v7.view.ActionMode?, item: MenuItem?): Boolean {
                    return false
                }

                override fun onCreateActionMode(mode: android.support.v7.view.ActionMode?, menu: Menu?): Boolean {
                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activity.window.statusBarColor = activity.resources.getColor(R.color.dark_flat_blue)
                    }
                    var inflater = mode!!.menuInflater
                    inflater.inflate(R.menu.edit_text_menu, menu)
                    return true
                }

                override fun onDestroyActionMode(mode: android.support.v7.view.ActionMode?) {
                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activity.window.statusBarColor = activity.resources.getColor(R.color.green_ripple)
                    }
                }
            })
        }

        deleteButton.setOnClickListener({
            activity.pendingItemDeleted(this)
        })

        dragHandle.setOnTouchListener({ view: View, motionEvent: MotionEvent ->
            if (MotionEventCompat.getActionMasked(motionEvent) ==
                    MotionEvent.ACTION_DOWN) {
                activity.startListItemDragWith(this)
            }

            return@setOnTouchListener false
        })
    }
}