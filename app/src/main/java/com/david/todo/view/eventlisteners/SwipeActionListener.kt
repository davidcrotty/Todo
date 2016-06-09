package com.david.todo.view.eventlisteners

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import timber.log.Timber

/**
 * Created by DavidHome on 07/06/2016.
 */
class SwipeActionListener : RecyclerView.OnItemTouchListener {

    val NO_TRANSLATION: Float = 0F
    var selectedViewForeground: View? = null

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
        Timber.d("onTouchEvent")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        if(e?.action == MotionEvent.ACTION_MOVE) {
            if(selectedViewForeground != null) {
                selectedViewForeground?.translationX = e?.rawX!!
                return false
            }

            var view = rv?.findChildViewUnder(e!!.x, e.y)
            if(view != null) {
                findAndSelectViewIfValidViewHolderItem(view, rv)
            }
        }

        if(e?.action == MotionEvent.ACTION_UP) {
            if(selectedViewForeground != null) { //ping view back to original position TODO check here no other animation was triggered
                selectedViewForeground?.translationX = NO_TRANSLATION
                Timber.d("Released view")
            }
            selectedViewForeground = null
        }

        return false
    }

    fun findAndSelectViewIfValidViewHolderItem(view: View, recyclerView: RecyclerView?) {
        if(selectedViewForeground == null) { //First time at selecting a view?
            var viewHolder = recyclerView?.getChildViewHolder(view) //get rv's view holder
            if(viewHolder is PendingItemViewHolder) { //are we the right type of viewholder? TODO (As a lib parent view could be of certain type
                var pendingItemHolder = viewHolder
                selectedViewForeground = pendingItemHolder.taskForeground
                Timber.d("View selected, translation ${selectedViewForeground?.translationX}")
            } else {
                selectedViewForeground = null
            }
        }
    }


    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        Timber.d("onRequestDisallowInterceptTouchEvent")
    }

}