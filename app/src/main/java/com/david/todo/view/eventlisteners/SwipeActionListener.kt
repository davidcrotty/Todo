package com.david.todo.view.eventlisteners

import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import timber.log.Timber

/**
 * Created by DavidHome on 07/06/2016.
 */
class SwipeActionListener : RecyclerView.OnItemTouchListener {

    val NO_TRANSLATION: Float = 0F
    var selectedViewForeground: ViewGroup? = null


    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
        Timber.d("onTouchEvent")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        when(e?.action) {
            MotionEvent.ACTION_MOVE -> {
                if(selectedViewForeground != null) {
                    selectedViewForeground?.translationX = e?.rawX!!
                    return false
                }

                findAndSelectViewIfValidViewHolderItem(rv?.findChildViewUnder(e!!.x, e.y), rv)
            }
            MotionEvent.ACTION_UP -> {
                if(selectedViewForeground != null) { //ping view back to original position TODO check here no other animation was triggered
                    selectedViewForeground?.translationX = NO_TRANSLATION
                    Timber.d("Released view")
                }
                selectedViewForeground = null
            }
        }

        return false
    }

    /**
     * Finds out if view is part of this rv's list and a valid item type, as only Pending
     * items are allowed to be the swiped
     */
    fun findAndSelectViewIfValidViewHolderItem(view: View?, recyclerView: RecyclerView?) {
        val view = view?.let { it } ?: return

        if(selectedViewForeground == null) { //First time at selecting a view?
            var viewHolder = recyclerView?.getChildViewHolder(view) //get rv's view holder
            if(viewHolder is PendingItemViewHolder) { //are we the right type of viewholder? TODO (As a lib parent view could be of certain type
                var pendingItemHolder = viewHolder
                selectViewHolder(pendingItemHolder.taskForeground)
                Timber.d("View selected, translation ${selectedViewForeground?.translationX}")
            } else {
                selectViewHolder(null)
            }
        }
    }

    /**
     * Selects viewholder, records it as a local variable as well as associates a delta value with it
     */
    fun selectViewHolder(view: ViewGroup?) {
        selectedViewForeground = view
    }


    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        Timber.d("onRequestDisallowInterceptTouchEvent")
    }

}