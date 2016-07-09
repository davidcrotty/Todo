package com.david.todo.view.eventlisteners

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import com.david.todo.R
import com.david.todo.adapter.ChecklistAdapter
import timber.log.Timber

/**
 * Created by DavidHome on 07/06/2016.
 */

/**
 * @param context - Used for getting view configuration in determining fling distance per device
 * @param checkListAdapter - Used for calling specific methods on the recycler adapter for handling removals
 */
class SwipeActionListener(val context: Context, val checkListAdapter: ChecklistAdapter, deleteToggleMargin: Float) : RecyclerView.OnItemTouchListener {

    companion object {
        val PENDING_TRANSLATE_X = 0F
        var DELETE_TOGGLE_MARGIN = 0F
    }

    lateinit var interactionHandler: InteractionHandler

    //Due to https://code.google.com/p/android/issues/detail?id=205947 ItemTouchHelper requires a manual
    // 'shouldPreventSwipeEvents' to prevent unwanted touch events firing off
    var shouldPreventSwipeEvents: Boolean = false

    init {
        DELETE_TOGGLE_MARGIN = deleteToggleMargin
        interactionHandler = InteractionHandler(checkListAdapter, context)
    }

    fun deselectInteractionItem() {
        interactionHandler.selectedInteractionItem = null
    }

    fun reset() {
        interactionHandler.reset()
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
        Timber.d("onTouchEvent")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, event: MotionEvent?): Boolean {
        if(shouldPreventSwipeEvents) return false
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {

                interactionHandler.beginInteractionWith(event = event,
                                                        recyclerView = rv)
            }
            MotionEvent.ACTION_MOVE -> {
                interactionHandler.translateSelectedViewWith(event)
            }
            MotionEvent.ACTION_UP -> {
                interactionHandler.finaliseInteractionWith(event)
            }
            MotionEvent.ACTION_CANCEL -> {
                Timber.d("ITEM - CANCEL")
            }
        }

        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        Timber.d("onRequestDisallowInterceptTouchEvent")
    }

}