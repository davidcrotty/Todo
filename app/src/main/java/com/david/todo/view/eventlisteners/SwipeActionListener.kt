package com.david.todo.view.eventlisteners

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import timber.log.Timber

/**
 * Created by DavidHome on 07/06/2016.
 */
class SwipeActionListener : RecyclerView.OnItemTouchListener {

    var selectedViewForeground: View? = null

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
        Timber.d("onTouchEvent")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {

        if(e?.action == MotionEvent.ACTION_MOVE) {
            if(selectedViewForeground != null) { //Already selected view?, move it and return
                Timber.d("Moving view")
                return false
            }

            var view = rv?.findChildViewUnder(e!!.x, e!!.y) //Find view holder
            if(view != null) { //Do we have a valid view
                if(selectedViewForeground == null) { //First time at selecting a view?
                    var viewHolder = rv?.getChildViewHolder(view) //get rv's view holder
                    if(viewHolder is PendingItemViewHolder) { //are we the right type of viewholder? TODO (As a lib parent view could be of certain type
                        var pendingItemHolder = viewHolder
                        selectedViewForeground = pendingItemHolder.taskForeground
                        Timber.d("View selected")
                    } else {
                        selectedViewForeground = null
                    }
                }
            }
        }

        if(e?.action == MotionEvent.ACTION_UP) { //Stopped interacting
            if(selectedViewForeground != null) { //ping view back to original position
                Timber.d("Released view")
            }
            selectedViewForeground = null
        }

        //rv finds event, can translate
        return false
    }


    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        Timber.d("onRequestDisallowInterceptTouchEvent")
    }

}